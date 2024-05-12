package me.draconix6.moveresizeplugin;

import com.google.common.io.Resources;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import me.draconix6.moveresizeplugin.command.CursorSpeedCommand;
import me.draconix6.moveresizeplugin.command.ResizeCommand;
import me.draconix6.moveresizeplugin.gui.EyeSeeGUI;
import me.draconix6.moveresizeplugin.lua.MoveResizeLuaLibrary;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiAppLaunch;
import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.command.CommandManager;
import xyz.duncanruns.julti.instance.GameOptions;
import xyz.duncanruns.julti.instance.MinecraftInstance;
import xyz.duncanruns.julti.plugin.PluginEvents;
import xyz.duncanruns.julti.plugin.PluginInitializer;
import xyz.duncanruns.julti.plugin.PluginManager;
import xyz.duncanruns.julti.script.lua.LuaLibraries;
import xyz.duncanruns.julti.util.WindowStateUtil;
import xyz.duncanruns.julti.win32.User32;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;

public class MoveResizePlugin implements PluginInitializer {
    public static int winStyle = 0;
    public static boolean wasFullscreen = false;
    public static Rectangle prevWindowSize = new Rectangle();
    public static int prevCursorSpeed = 0;
    private static EyeSeeGUI gui = null;

    public static void main(String[] args) throws IOException {
        // This is only used to test the plugin in the dev environment
        // ExamplePlugin.main itself is never used when users run Julti

        JultiAppLaunch.launchWithDevPlugin(args, PluginManager.JultiPluginData.fromString(
                Resources.toString(Resources.getResource(MoveResizePlugin.class, "/julti.plugin.json"), Charset.defaultCharset())
        ), new MoveResizePlugin());
    }

    public static EyeSeeGUI getESGui() {
        if (gui == null) {
            gui = new EyeSeeGUI();
            gui.hideEyeSee();
        }
        return gui;
    }

    /**
     * @param instance     the instance to resize
     * @param width        the target width
     * @param height       the target height
     * @param useMagnifier if an EyeSee window should be used
     *
     * @return true if the instance was resized
     */
    public static boolean toggleResize(MinecraftInstance instance, int width, int height, boolean useMagnifier) {
        boolean stretching = true;
        JultiOptions options = JultiOptions.getJultiOptions();
        Rectangle currentBounds = WindowStateUtil.getHwndRectangle(instance.getHwnd());
        Rectangle boundsToSet = new Rectangle(options.windowPos[0], options.windowPos[1], width, height);
        if (currentBounds.width == boundsToSet.width && currentBounds.height == boundsToSet.height) {
            boundsToSet.width = options.playingWindowSize[0];
            boundsToSet.height = options.playingWindowSize[1];
            stretching = false;
        }
        if (options.windowPosIsCenter) {
            boundsToSet = WindowStateUtil.withTopLeftToCenter(boundsToSet);
        }

        if (!stretching) {
            if (wasFullscreen) { // && useMagnifier
                GameOptions go = instance.getGameOptions();
                instance.getKeyPresser().pressKey(go.fullscreenKey);
            }
            WindowStateUtil.setHwndStyle(instance.getHwnd(), winStyle);
        } else {
            // if (useMagnifier) {
            wasFullscreen = instance.isFullscreen();
            if (wasFullscreen) {
                instance.ensureNotFullscreen();
            }
            // }
            winStyle = WindowStateUtil.getHwndStyle(instance.getHwnd());
            WindowStateUtil.setHwndBorderless(instance.getHwnd());
        }

        // credits to priffin/tallmacro
        if (User32.INSTANCE.SetWindowPos(
                instance.getHwnd(),
                new WinDef.HWND(new Pointer(0)),
                boundsToSet.x,
                boundsToSet.y,
                boundsToSet.width,
                boundsToSet.height,
                new WinDef.UINT(0x0400))
        ) {
            if (useMagnifier) {
                if (!stretching) {
                    getESGui().hideEyeSee();
                } else getESGui().showEyeSee(boundsToSet, instance);
            }
            User32.INSTANCE.SetForegroundWindow(instance.getHwnd());
        }
        return stretching;
    }

    @Override
    public void initialize() {
        // This gets run once when Julti launches
        PluginEvents.RunnableEventType.RELOAD.register(() -> {
            // This gets run when Julti launches and every time the profile is switched
            Julti.log(Level.INFO, "Move & Resize Plugin Reloaded!");
        });

        PluginEvents.InstanceEventType.RESET.register((MinecraftInstance inst) -> {
            if (gui != null) gui.hideEyeSee();
//            inst.ensureResettingWindowState(true);
        });

        getESGui();

        CommandManager.getMainManager().registerCommand(new ResizeCommand());
        CommandManager.getMainManager().registerCommand(new CursorSpeedCommand());
        LuaLibraries.registerLuaLibrary(MoveResizeLuaLibrary::new);
        Julti.log(Level.INFO, "Move & Resize Plugin Initialized");
    }

    @Override
    public void onMenuButtonPress() {
    }

    @Override
    public boolean hasMenuButton() {
        return false;
    }
}
