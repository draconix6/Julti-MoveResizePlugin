package me.draconix6.moveresizeplugin;

import com.google.common.io.Resources;
import org.apache.logging.log4j.Level;
import eyesee.EyeSeeGUI;
import win32.HwndUtil;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiAppLaunch;
import xyz.duncanruns.julti.gui.JultiGUI;
import xyz.duncanruns.julti.plugin.PluginInitializer;
import xyz.duncanruns.julti.plugin.PluginManager;
import xyz.duncanruns.julti.util.WindowStateUtil;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;

public class MoveResizePlugin implements PluginInitializer {
    public static EyeSeeGUI gui = new EyeSeeGUI();
    public static int winStyle = 0;
    public static int prevCursorSpeed = 0;
    public static boolean changedCursorSpeed = false;

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
        InitPlugin.init();
        gui.hideEyeSee();
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
}
