package xyz.duncanruns.eyesee;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import org.apache.logging.log4j.Level;
import me.draconix6.moveresizeplugin.win32.User32;
import me.draconix6.moveresizeplugin.win32.GDI32Extra;
import me.draconix6.moveresizeplugin.win32.HwndUtil;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.util.MonitorUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author DuncanRuns
 */
public class EyeSeeGUI extends JFrame implements WindowListener {

    private static final WinDef.DWORD SRCCOPY = new WinDef.DWORD(0x00CC0020);

    private OverlayGUI overlay;

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private WinDef.HWND sourceHwnd;
    private final WinDef.HWND eyeSeeHwnd;
    private boolean currentlyShowing = false;
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);

    public EyeSeeGUI() {
        super();
        addWindowListener(this);
        setResizable(false);
        String randTitle = "Julti EyeSee " + new Random().nextInt();
        setTitle(randTitle);
        setVisible(true);
        eyeSeeHwnd = new WinDef.HWND(HwndUtil.waitForWindow(randTitle));
        setTitle("Julti EyeSee");
        HwndUtil.setHwndBorderless(eyeSeeHwnd.getPointer());
        tick();
        // 30 = refresh rate
        // TODO: adjustable?
        executor.scheduleAtFixedRate(this::tick, 50_000_000, 1_000_000_000L / 30, TimeUnit.NANOSECONDS);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
    }

    private void tick() {
        if (!currentlyShowing) return;

        if (sourceHwnd == null) return;
        Rectangle rectangle = getYoinkArea(sourceHwnd);
        WinDef.HDC sourceHDC = me.draconix6.moveresizeplugin.win32.User32.INSTANCE.GetDC(sourceHwnd);
        WinDef.HDC eyeSeeHDC = me.draconix6.moveresizeplugin.win32.User32.INSTANCE.GetDC(eyeSeeHwnd);

        GDI32Extra.INSTANCE.SetStretchBltMode(eyeSeeHDC, 3);

        GDI32Extra.INSTANCE.StretchBlt(eyeSeeHDC, 0, 0, bounds.width, bounds.height, sourceHDC, rectangle.x, rectangle.y, rectangle.width, rectangle.height, SRCCOPY);

//        Image logo = Toolkit.getDefaultToolkit().getImage(JultiOptions.getJultiDir().resolve("overlay.png").toString());
//        WinDef.HDC overlayHDC = me.draconix6.moveresizeplugin.win32.User32.INSTANCE.

//        JPanel pane = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.drawImage(logo, 0, 0, bounds.width, bounds.height, null);
//            }
//        };
//
//        this.add(pane);

//        User32.INSTANCE.ReleaseDC(sourceHwnd, sourceHDC);
//        User32.INSTANCE.ReleaseDC(eyeSeeHwnd, eyeSeeHDC);
    }

    public void showEyeSee(Rectangle zoomRect) {
        System.out.println("Showing EyeSee...");
        if (sourceHwnd == null) sourceHwnd = new WinDef.HWND(me.draconix6.moveresizeplugin.win32.User32.INSTANCE.GetForegroundWindow());
        currentlyShowing = true;
        setVisible(true);
        setAlwaysOnTop(true);

        // TODO: readd manual setting of these values later !
        // credit to priffin for these calculations
        MonitorUtil.Monitor monitor = MonitorUtil.getPrimaryMonitor();
        int projectorWidth = (monitor.width - zoomRect.width) / 2;
        int projectorHeight = (int)(projectorWidth / (16.0f / 9.0f)); // this was weird, just sticking to 16:9

        if (projectorWidth == 0 || projectorHeight == 0) {
            Julti.log(Level.WARN, "Not enough room to create EyeSee window - please decrease your resize width!"); // TODO: "or manually change your EyeSee window settings!"
            return;
        }

        this.bounds.width = projectorWidth;
        this.bounds.height = projectorHeight;

        int projectorXPos = 0;
        int projectorYPos = (monitor.height - projectorHeight) / 2;

        // move xyz.duncanruns.eyesee window
        User32.INSTANCE.SetWindowPos(
                eyeSeeHwnd.getPointer(),
                new WinDef.HWND(new Pointer(0)).getPointer(),
                projectorXPos,
                projectorYPos,
                projectorWidth,
                projectorHeight,
                new WinDef.UINT(0x0400)
        );

        this.overlay = new OverlayGUI();
        this.overlay.setVisible(true);
        this.overlay.setAlwaysOnTop(true);
        WinDef.HWND overlayHwnd = new WinDef.HWND(HwndUtil.waitForWindow("EyeSee Overlay"));
        HwndUtil.setHwndBorderless(overlayHwnd.getPointer());

        // add overlay image & resize accordingly
        Image image = this.overlay.icon.getImage();
        Image newImage = image.getScaledInstance(projectorWidth, projectorHeight, Image.SCALE_SMOOTH);
        this.overlay.icon = new ImageIcon(newImage);
        this.overlay.label1 = new JLabel(this.overlay.icon);
        this.overlay.add(this.overlay.label1);

        this.overlay.setSize(projectorWidth, projectorHeight);
        this.overlay.setLocation(projectorXPos, projectorYPos);
    }

    public void hideEyeSee() {
        System.out.println("Hiding EyeSee...");
        currentlyShowing = false;
        setVisible(false);

        MonitorUtil.Monitor monitor = MonitorUtil.getPrimaryMonitor();
        User32.INSTANCE.SetWindowPos(
                eyeSeeHwnd.getPointer(),
                new WinDef.HWND(new Pointer(0)).getPointer(),
                0,
                -monitor.height,
                1,
                1,
                new WinDef.UINT(0x0400)
        );

        if (this.overlay == null) return;
        this.overlay.setVisible(false);
    }

    @Override
    public boolean isShowing() {
        return currentlyShowing;
    }

    private Rectangle getYoinkArea(WinDef.HWND hwnd) {
        Rectangle rectangle;
        if (hwnd == null) {
            rectangle = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        } else {
            rectangle = HwndUtil.getHwndInnerRectangle(hwnd.getPointer());
        }
        // TODO: figure these out better
        int width = 60;
        int height = 580;
        return new Rectangle((int) rectangle.getCenterX() - width / 2, (int) rectangle.getCenterY() - height / 2, width, height);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        executor.shutdownNow();
        System.out.println("EyeSee Closed.");
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
