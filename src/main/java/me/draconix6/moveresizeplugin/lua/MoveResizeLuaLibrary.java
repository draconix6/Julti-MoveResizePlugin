package me.draconix6.moveresizeplugin.lua;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import me.draconix6.moveresizeplugin.MoveResizePlugin;
import me.draconix6.moveresizeplugin.win32.User32Extra;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.cancelrequester.CancelRequester;
import xyz.duncanruns.julti.instance.MinecraftInstance;
import xyz.duncanruns.julti.management.InstanceManager;
import xyz.duncanruns.julti.script.lua.LuaLibrary;
import xyz.duncanruns.julti.util.WindowStateUtil;
import xyz.duncanruns.julti.win32.User32;

import java.awt.*;

/**
 * @author DuncanRuns
 */
@SuppressWarnings("unused")
public class MoveResizeLuaLibrary extends LuaLibrary {
    public MoveResizeLuaLibrary(CancelRequester requester) {
        super(requester, "moveresize");
    }

    private static MinecraftInstance getInstanceFromInt(int instanceNum) {
        synchronized (Julti.getJulti()) {
            return InstanceManager.getInstanceManager().getInstances().get(instanceNum - 1);
        }
    }

    @LuaDocumentation(description = "Gets the current windows cursor speed.")
    public int getCursorSpeed() {
        IntByReference ref = new IntByReference(0);
        User32Extra.INSTANCE.SystemParametersInfoA(0x70, 0, ref, 0);
        return ref.getValue();
    }

    @LuaDocumentation(description = "Sets the current windows cursor speed.")
    public void setCursorSpeed(int speed) {
        User32Extra.INSTANCE.SystemParametersInfoA(0x71, 0, speed, 0);
    }

    @LuaDocumentation(description = "Hides the EyeSee window.")
    public void hideEyeSee(Integer showInstanceAfter) {
        synchronized (Julti.getJulti()) {
            MoveResizePlugin.getESGui().hideEyeSee();
            if (showInstanceAfter != null) {
                User32.INSTANCE.SetForegroundWindow(getInstanceFromInt(showInstanceAfter).getHwnd());
            }
        }
    }

    @LuaDocumentation(description = "Shows the EyeSee window.")
    public void showEyeSee(int instanceNum, int instanceWidth, int instanceHeight) {
        synchronized (Julti.getJulti()) {
            MinecraftInstance instance = getInstanceFromInt(instanceNum);
            JultiOptions options = JultiOptions.getJultiOptions();
            Rectangle instanceBounds = new Rectangle(options.windowPos[0], options.windowPos[1], instanceWidth, instanceHeight);
            if (options.windowPosIsCenter) {
                instanceBounds = WindowStateUtil.withTopLeftToCenter(instanceBounds);
            }
            MoveResizePlugin.getESGui().showEyeSee(instanceBounds, instance);
            User32.INSTANCE.SetForegroundWindow(instance.getHwnd());
        }
    }

    @LuaDocumentation(description = "Returns true if the EyeSee window is currently being shown, otherwise false.")
    public boolean isEyeSeeShown() {
        synchronized (Julti.getJulti()) {
            return MoveResizePlugin.getESGui().isShowing();
        }
    }

    @LuaDocumentation(description = "Gets the size of the specified instance.", returnTypes = {"number", "number"})
    public Varargs getSize(int instanceNum) {
        synchronized (Julti.getJulti()) {
            Rectangle hwndRectangle = WindowStateUtil.getHwndRectangle(getInstanceFromInt(instanceNum).getHwnd());
            return varargsOf(new LuaValue[]{valueOf(hwndRectangle.width), valueOf(hwndRectangle.height)});
        }
    }

    @LuaDocumentation(description = "Gets the playing size from Julti options.", returnTypes = {"number", "number"})
    public Varargs getPlayingSize() {
        synchronized (Julti.getJulti()) {
            int[] playingWindowSize = JultiOptions.getJultiOptions().playingWindowSize;
            return varargsOf(new LuaValue[]{valueOf(playingWindowSize[0]), valueOf(playingWindowSize[1])});
        }
    }

    @LuaDocumentation(description = "Resizes the specified instance to the specified width/height.\nThe window position or center window position will remain the same depending on the windowPosIsCenter option.")
    public void resizeInstance(int instanceNum, int width, int height) {
        synchronized (Julti.getJulti()) {
            JultiOptions options = JultiOptions.getJultiOptions();
            Rectangle boundsToSet = new Rectangle(options.windowPos[0], options.windowPos[1], width, height);
            if (options.windowPosIsCenter) {
                boundsToSet = WindowStateUtil.withTopLeftToCenter(boundsToSet);
            }
            // credits to priffin/tallmacro
            User32.INSTANCE.SetWindowPos(
                    getInstanceFromInt(instanceNum).getHwnd(),
                    new WinDef.HWND(new Pointer(0)),
                    boundsToSet.x,
                    boundsToSet.y,
                    boundsToSet.width,
                    boundsToSet.height,
                    new WinDef.UINT(0x0400));
        }
    }

    @LuaDocumentation(description = "Gets the window style for the specified instance.")
    public int getInstanceWStyle(int instanceNum) {
        synchronized (Julti.getJulti()) {
            return WindowStateUtil.getHwndStyle(getInstanceFromInt(instanceNum).getHwnd());
        }
    }

    @LuaDocumentation(description = "Sets the window style for the specified instance.")
    public void setInstanceWStyle(int instanceNum, int wStyle) {
        synchronized (Julti.getJulti()) {
            WindowStateUtil.setHwndStyle(getInstanceFromInt(instanceNum).getHwnd(), wStyle);
        }
    }

    @LuaDocumentation(description = "Sets the specified instance to borderless.")
    public void setBorderless(int instanceNum) {
        synchronized (Julti.getJulti()) {
            WindowStateUtil.setHwndBorderless(getInstanceFromInt(instanceNum).getHwnd());
        }
    }

    @LuaDocumentation(description = "Presses the fullscreen hotkey on the specified instance.")
    public void pressFullscreen(int instanceNum) {
        synchronized (Julti.getJulti()) {
            MinecraftInstance instance = getInstanceFromInt(instanceNum);
            instance.getKeyPresser().pressKey(instance.getGameOptions().fullscreenKey);
        }
    }

    @LuaDocumentation(description = "Attempts to ensure an instance is not in fullscreen and wait until it unfullscreens.")
    public void ensureNotFullscreen(int instanceNum) {
        synchronized (Julti.getJulti()) {
            getInstanceFromInt(instanceNum).ensureNotFullscreen();
        }
    }

    @LuaDocumentation(description = "Checks if the specified instance is in fullscreen.")
    public boolean isFullscreen(int instanceNum) {
        synchronized (Julti.getJulti()) {
            return getInstanceFromInt(instanceNum).isFullscreen();
        }
    }

    @LuaDocumentation(description = "Runs the toggle resize functionality. The return value will be true if the instance is resized to the specified value and false if the resize is reversed back to the playing window size.")
    public boolean toggleResize(int instanceNum, int width, int height, boolean useMagnifier) {
        synchronized (Julti.getJulti()) {
            return MoveResizePlugin.toggleResize(getInstanceFromInt(instanceNum), width, height, useMagnifier);
        }
    }
}
