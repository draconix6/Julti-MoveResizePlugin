package me.draconix6.moveresizeplugin;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.cancelrequester.CancelRequester;
import xyz.duncanruns.julti.command.Command;
import xyz.duncanruns.julti.command.CommandFailedException;
import xyz.duncanruns.julti.instance.MinecraftInstance;
import xyz.duncanruns.julti.management.InstanceManager;
import xyz.duncanruns.julti.util.WindowStateUtil;
import win32.User32;

import java.awt.*;

public class CursorSpeedCommand extends Command {

    @Override
    public String helpDescription() {
//        return "cursorspeed [speed] - Changes the Windows cursor speed. Saves the previous cursor speed to toggle back.\n" +
//                "cursorspeed [speed] [initialspeed] - Same as above, but doesn't save the previous speed in favour of reverting to the given initialspeed value.";
        return "cursorspeed [speed1] [speed2] - Changes the Windows cursor speed between the given values.";
    }

    @Override
    public int getMinArgs() {
        return 2;
    } // TODO: fix & allow for one arg

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public String getName() {
        return "cursorspeed";
    }

    @Override
    public void run(String[] args, CancelRequester cancelRequester) {
        // credits to Priffin againe
        int currentSpeed = 0;
        User32.INSTANCE.SystemParametersInfoA(0x70, 0, currentSpeed, 0);
        Julti.log(Level.DEBUG, "Current cursor speed: " + Integer.toString(currentSpeed));

        // has explicit initial speed - set to it
        // if ((args.length > 1 && currentSpeed != Integer.parseInt(args[1]))) {
        if (args.length > 1) {
            if (MoveResizePlugin.changedCursorSpeed) {
                User32.INSTANCE.SystemParametersInfoA(0x71, 0, Integer.parseInt(args[1]), 0);
                MoveResizePlugin.changedCursorSpeed = false;
            }
            else {
                User32.INSTANCE.SystemParametersInfoA(0x71, 0, Integer.parseInt(args[0]), 0);
                MoveResizePlugin.changedCursorSpeed = true;
            }
            return;
        }
        // changing speed from default - save & change cursor speed
        else if (MoveResizePlugin.prevCursorSpeed == 0 && args.length < 2) {
            MoveResizePlugin.prevCursorSpeed = currentSpeed;
        }
        // returning to saved speed
        else if (currentSpeed != MoveResizePlugin.prevCursorSpeed) {
            User32.INSTANCE.SystemParametersInfoA(0x71, 0, MoveResizePlugin.prevCursorSpeed, 0);
            return;
        }
        User32.INSTANCE.SystemParametersInfoA(0x71, 0, Integer.parseInt(args[0]), 0);
    }
}
