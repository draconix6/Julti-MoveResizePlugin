package me.draconix6.moveresizeplugin.gui;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.win32.User32;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class OverlayGUI extends JFrame {
    public ImageIcon icon;
    public JLabel label;
    public WinDef.HWND hwnd;

    public OverlayGUI() {
        this.setTitle("EyeSee Overlay");
        this.setType(Type.UTILITY);
        this.setUndecorated(true);
        this.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setSize(800, 420);
        panel.setOpaque(false);
        this.setContentPane(panel);

        this.setVisible(true);
        this.hwnd = new WinDef.HWND(Native.getWindowPointer(this));
        User32.INSTANCE.BringWindowToTop(this.hwnd);

        this.icon = new ImageIcon(getImageUrl());
        this.label = new JLabel(this.icon);

        this.setVisible(false);
    }

    private URL getImageUrl() {
        try {
            Path path = JultiOptions.getJultiDir().resolve("eyesee_overlay.png");
            File file = path.toFile();
            if (!file.exists()) {
                Files.copy(Objects.requireNonNull(this.getClass().getResourceAsStream("/overlay.png")), path);
            }
            if (file.isFile()) {
                return file.toURI().toURL();
            }
        } catch (IOException ignored) {}
        return Objects.requireNonNull(this.getClass().getResource("/overlay.png"));
    }
}
