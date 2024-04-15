package me.draconix6.moveresizeplugin.command;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import me.draconix6.moveresizeplugin.MoveResizePlugin;
import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.cancelrequester.CancelRequester;
import xyz.duncanruns.julti.command.Command;
import xyz.duncanruns.julti.command.CommandFailedException;
import xyz.duncanruns.julti.instance.GameOptions;
import xyz.duncanruns.julti.instance.MinecraftInstance;
import xyz.duncanruns.julti.management.InstanceManager;
import xyz.duncanruns.julti.util.WindowStateUtil;
import xyz.duncanruns.julti.win32.User32;

import java.awt.*;

public class ResizeCommand extends Command {

    @Override
    public String helpDescription() {
        return "resize [width] [height] - Resizes the window of the active instance to the specified size - if window is already resized, revert to the playing window size.\n" +
                "resize [width] [height] zoom - Same as above, but also generates a window with an overlay to assist with eye measurements (i.e. Priffin Mag projector)";
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public int getMaxArgs() {
        return 3;
    }

    @Override
    public String getName() {
        return "resize";
    }

    @Override
    public void run(String[] args, CancelRequester cancelRequester) {
        MinecraftInstance activeInstance = InstanceManager.getInstanceManager().getSelectedInstance();
        if (activeInstance == null) {
            throw new CommandFailedException("Instance is not active, cannot resize - please add the command as a script and assign a hotkey to it as per the instructions on the GitHub.");
        }

        JultiOptions options = JultiOptions.getJultiOptions();
        WinDef.HWND mcHwnd = activeInstance.getHwnd();

        boolean stretching = true;
        boolean useMagnifier = args.length > 2 && args[2].equals("zoom");

        Rectangle currentBounds = WindowStateUtil.getHwndRectangle(mcHwnd);
        Rectangle boundsToSet = new Rectangle(options.windowPos[0], options.windowPos[1], Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        if (currentBounds.width == boundsToSet.width && currentBounds.height == boundsToSet.height) {
            boundsToSet.width = options.playingWindowSize[0];
            boundsToSet.height = options.playingWindowSize[1];
            stretching = false;
        }
        if (options.windowPosIsCenter) {
            boundsToSet = WindowStateUtil.withTopLeftToCenter(boundsToSet);
        }

        if (!stretching) {
            if (MoveResizePlugin.wasFullscreen && useMagnifier) {
                GameOptions go = activeInstance.getGameOptions();
                activeInstance.getKeyPresser().pressKey(go.fullscreenKey);
            }
            WindowStateUtil.setHwndStyle(mcHwnd, MoveResizePlugin.winStyle);
        } else {
            if (useMagnifier) {
                MoveResizePlugin.wasFullscreen = activeInstance.isFullscreen();
                if (MoveResizePlugin.wasFullscreen) {
                    activeInstance.ensureNotFullscreen();
                }
            }
            MoveResizePlugin.winStyle = WindowStateUtil.getHwndStyle(mcHwnd);
            WindowStateUtil.setHwndBorderless(mcHwnd);
        }

        // credits to priffin/tallmacro
        if (User32.INSTANCE.SetWindowPos(
                mcHwnd,
                new WinDef.HWND(new Pointer(0)),
                boundsToSet.x,
                boundsToSet.y,
                boundsToSet.width,
                boundsToSet.height,
                new WinDef.UINT(0x0400))
        ) {
            if (args.length > 2 && args[2].equals("zoom")) {
                if (!stretching) MoveResizePlugin.getESGui().hideEyeSee();
                else MoveResizePlugin.getESGui().showEyeSee(boundsToSet, activeInstance);
            }
            User32.INSTANCE.SetForegroundWindow(mcHwnd);
        }
    }
}
