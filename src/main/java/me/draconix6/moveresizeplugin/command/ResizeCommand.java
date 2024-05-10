package me.draconix6.moveresizeplugin.command;

import me.draconix6.moveresizeplugin.MoveResizePlugin;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.cancelrequester.CancelRequester;
import xyz.duncanruns.julti.command.Command;
import xyz.duncanruns.julti.command.CommandFailedException;
import xyz.duncanruns.julti.instance.MinecraftInstance;
import xyz.duncanruns.julti.management.InstanceManager;

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
        MinecraftInstance instance = InstanceManager.getInstanceManager().getSelectedInstance();
        if (instance == null) {
            throw new CommandFailedException("Instance is not active, cannot resize - please add the command as a script and assign a hotkey to it as per the instructions on the GitHub.");
        }

        if (args.length > 2 && !args[2].equals("zoom")) {
            throw new CommandFailedException("Unknown argument given to resize command: " + args[2]);
        }

        boolean useMagnifier = args.length > 2;
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        
        synchronized (Julti.getJulti()) {
            MoveResizePlugin.toggleResize(instance, width, height, useMagnifier);
        }
    }
}
