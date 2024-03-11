package me.draconix6.moveresizeplugin.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class OverlayGUI extends JFrame {
    public ImageIcon icon;
    public JLabel label;

    public OverlayGUI() {
        this.setTitle("EyeSee Overlay");
        this.setUndecorated(true);
        this.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setSize(800, 420);
        panel.setOpaque(false);
        this.setContentPane(panel);

        this.setVisible(false);

        this.icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/overlay.png")));
        this.label = new JLabel(this.icon);
    }
}
