package me.draconix6.moveresizeplugin.lua;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import me.draconix6.moveresizeplugin.MoveResizePlugin;
import me.draconix6.moveresizeplugin.win32.User32Extra;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
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

    // (Not a lua function)
    private static MinecraftInstance getInstanceFromInt(int instanceNum) {
        return InstanceManager.getInstanceManager().getInstances().get(instanceNum - 1);
    }

    public int getCursorSpeed() {
        IntByReference ref = new IntByReference(0);
        User32Extra.INSTANCE.SystemParametersInfoA(0x70, 0, ref, 0);
        return ref.getValue();
    }

    public void setCursorSpeed(int speed) {
        User32Extra.INSTANCE.SystemParametersInfoA(0x71, 0, speed, 0);
    }

    public void hideEyeSee(Integer showInstanceAfter) {
        MoveResizePlugin.getESGui().hideEyeSee();
        if (showInstanceAfter != null) {
            User32.INSTANCE.SetForegroundWindow(getInstanceFromInt(showInstanceAfter).getHwnd());
        }
    }

    public void showEyeSee(int instanceNum, int instanceWidth, int instanceHeight) {
        MinecraftInstance instance = getInstanceFromInt(instanceNum);
        JultiOptions options = JultiOptions.getJultiOptions();
        Rectangle instanceBounds = new Rectangle(options.windowPos[0], options.windowPos[1], instanceWidth, instanceHeight);
        if (options.windowPosIsCenter) {
            instanceBounds = WindowStateUtil.withTopLeftToCenter(instanceBounds);
        }
        MoveResizePlugin.getESGui().showEyeSee(instanceBounds, instance);
        User32.INSTANCE.SetForegroundWindow(instance.getHwnd());
    }

    public boolean isEyeSeeShown() {
        return MoveResizePlugin.getESGui().isShowing();
    }

    public Varargs getSize(int instanceNum) {
        Rectangle hwndRectangle = WindowStateUtil.getHwndRectangle(getInstanceFromInt(instanceNum).getHwnd());
        return varargsOf(new LuaValue[]{valueOf(hwndRectangle.width), valueOf(hwndRectangle.height)});
    }

    public Varargs getPlayingSize() {
        int[] playingWindowSize = JultiOptions.getJultiOptions().playingWindowSize;
        return varargsOf(new LuaValue[]{valueOf(playingWindowSize[0]), valueOf(playingWindowSize[1])});
    }

    public void resizeInstance(int instanceNum, int width, int height) {
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

    public int getInstanceWStyle(int instanceNum) {
        return WindowStateUtil.getHwndStyle(getInstanceFromInt(instanceNum).getHwnd());
    }

    public void setInstanceWStyle(int instanceNum, int wStyle) {
        WindowStateUtil.setHwndStyle(getInstanceFromInt(instanceNum).getHwnd(), wStyle);
    }

    public void setBorderless(int instanceNum) {
        WindowStateUtil.setHwndBorderless(getInstanceFromInt(instanceNum).getHwnd());
    }

    public void pressFullscreen(int instanceNum) {
        MinecraftInstance instance = getInstanceFromInt(instanceNum);
        instance.getKeyPresser().pressKey(instance.getGameOptions().fullscreenKey);
    }

    public void ensureNotFullscreen(int instanceNum) {
        getInstanceFromInt(instanceNum).ensureNotFullscreen();
    }

    public boolean isFullscreen(int instanceNum) {
        return getInstanceFromInt(instanceNum).isFullscreen();
    }
}
