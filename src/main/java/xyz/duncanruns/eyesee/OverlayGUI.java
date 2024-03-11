package xyz.duncanruns.eyesee;

import javax.swing.*;
import java.awt.*;

public class OverlayGUI extends JFrame {

    public ImageIcon icon;
    public JLabel label1;

    public OverlayGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));
        panel.setBackground(new Color(-16777216));
        panel.setEnabled(true);
        panel.setForeground(new Color(-16777216));
        this.setContentPane(panel);
        this.setTitle("EyeSee Overlay");
        this.setUndecorated(true);
        this.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));

        panel.setSize(800, 420);
        panel.setOpaque(false);

        this.setContentPane(panel);
        this.setLocationRelativeTo(null);

        this.icon = new ImageIcon(this.getClass().getResource("/overlay.png"));
        this.label1 = new JLabel(this.icon);
    }
}
