package me.draconix6.moveresizeplugin.command;

import com.sun.jna.ptr.IntByReference;
import me.draconix6.moveresizeplugin.MoveResizePlugin;
import me.draconix6.moveresizeplugin.win32.User32Extra;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.cancelrequester.CancelRequester;
import xyz.duncanruns.julti.command.Command;

public class CursorSpeedCommand extends Command {

    @Override
    public String helpDescription() {
//        return "cursorspeed [speed] - Changes the Windows cursor speed. Saves the previous cursor speed to toggle back.\n" +
//                "cursorspeed [speed] [initialspeed] - Same as above, but doesn't save the previous speed in favour of reverting to the given initialspeed value.";
        return "cursorspeed [speed1] [speed2] - Changes the Windows cursor speed between the given values.";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public String getName() {
        return "cursorspeed";
    }

    @SuppressWarnings("")
    @Override
    public void run(String[] args, CancelRequester cancelRequester) {
        // credits to Priffin againe
        int currentSpeed = getCurrentCursorSpeed();
        Julti.log(Level.DEBUG, "Current cursor speed: " + currentSpeed);

        // has explicit initial speed - set to it
         if ((args.length > 1 && currentSpeed != Integer.parseInt(args[1]))) {
            User32Extra.INSTANCE.SystemParametersInfoA(0x71, 0, Integer.parseInt(args[1]), 0);
            return;
        }
        // changing speed from default - save & change cursor speed
        else if (MoveResizePlugin.prevCursorSpeed == 0) {
            MoveResizePlugin.prevCursorSpeed = currentSpeed;
        }
        // returning to saved speed
        else if (currentSpeed != MoveResizePlugin.prevCursorSpeed) {
            User32Extra.INSTANCE.SystemParametersInfoA(0x71, 0, MoveResizePlugin.prevCursorSpeed, 0);
            return;
        }
        User32Extra.INSTANCE.SystemParametersInfoA(0x71, 0, Integer.parseInt(args[0]), 0);
    }

    /**
     * @author DuncanRuns
     */
    private static int getCurrentCursorSpeed(){
        IntByReference ref = new IntByReference(0);
        User32Extra.INSTANCE.SystemParametersInfoA(0x70, 0, ref, 0);
        return ref.getValue();
    }
}
