package me.draconix6.moveresizeplugin.gui;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.win32.User32;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class OverlayGUI extends JFrame {
    public ImageIcon icon;
    public JLabel label;
    public WinDef.HWND hwnd;

    public OverlayGUI() {
        this.setTitle("EyeSee Overlay");
        this.setUndecorated(true);
        this.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setSize(800, 420);
        panel.setOpaque(false);
        this.setContentPane(panel);

        this.setVisible(true);

        this.hwnd = new WinDef.HWND(Native.getWindowPointer(this));

        this.icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/overlay.png")));
        this.label = new JLabel(this.icon);
    }
}
