package me.draconix6.moveresizeplugin;

import me.draconix6.moveresizeplugin.command.CursorSpeedCommand;
import me.draconix6.moveresizeplugin.command.ResizeCommand;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.command.CommandManager;
import xyz.duncanruns.julti.plugin.PluginEvents;

public class InitPlugin {
    public static void init() {
        PluginEvents.RunnableEventType.RELOAD.register(() -> {
            // This gets run when Julti launches and every time the profile is switched
            Julti.log(Level.INFO, "Move & Resize Plugin Reloaded!");
        });

        CommandManager.getMainManager().registerCommand(new ResizeCommand());
        CommandManager.getMainManager().registerCommand(new CursorSpeedCommand());
    }
}
