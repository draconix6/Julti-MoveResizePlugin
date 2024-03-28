package me.draconix6.moveresizeplugin;

import com.google.common.io.Resources;
import me.draconix6.moveresizeplugin.command.CursorSpeedCommand;
import me.draconix6.moveresizeplugin.command.ResizeCommand;
import me.draconix6.moveresizeplugin.gui.EyeSeeGUI;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiAppLaunch;
import xyz.duncanruns.julti.command.CommandManager;
import xyz.duncanruns.julti.gui.JultiGUI;
import xyz.duncanruns.julti.plugin.PluginEvents;
import xyz.duncanruns.julti.plugin.PluginInitializer;
import xyz.duncanruns.julti.plugin.PluginManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;

public class MoveResizePlugin implements PluginInitializer {
    public static int winStyle = 0;
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

    @Override
    public void initialize() {
        // This gets run once when Julti launches
        PluginEvents.RunnableEventType.RELOAD.register(() -> {
            // This gets run when Julti launches and every time the profile is switched
            Julti.log(Level.INFO, "Move & Resize Plugin Reloaded!");
        });

        getESGui();

        CommandManager.getMainManager().registerCommand(new ResizeCommand());
        CommandManager.getMainManager().registerCommand(new CursorSpeedCommand());
        Julti.log(Level.INFO, "Move & Resize Plugin Initialized");
    }

    @Override
    public String getMenuButtonName() {
        return "Coming Soon";
    }

    @Override
    public void onMenuButtonPress() {
        JOptionPane.showMessageDialog(JultiGUI.getPluginsGUI(), "More config coming soon, check github.com/draconix6/Julti-MoveResizePlugin for updates.", "Julti Move Resize Plugin", JOptionPane.INFORMATION_MESSAGE);
    }

    public static EyeSeeGUI getESGui() {
        if (gui == null) {
            gui = new EyeSeeGUI();
            gui.hideEyeSee();
        }
        return gui;
    }
}
