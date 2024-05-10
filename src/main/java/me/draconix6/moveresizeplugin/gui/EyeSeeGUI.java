package me.draconix6.moveresizeplugin.gui;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import me.draconix6.moveresizeplugin.MoveResizePlugin;
import me.draconix6.moveresizeplugin.win32.GDI32Extra;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.instance.MinecraftInstance;
import xyz.duncanruns.julti.util.WindowStateUtil;
import xyz.duncanruns.julti.win32.User32;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author DuncanRuns
 */
public class EyeSeeGUI extends JFrame implements WindowListener {
    private static final WinDef.DWORD SRCCOPY = new WinDef.DWORD(0x00CC0020);
    private final OverlayGUI overlay = new OverlayGUI();
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private MinecraftInstance activeInst;

    private final WinDef.HWND eyeSeeHwnd;
    private boolean currentlyShowing = false;
    private final Rectangle bounds = new Rectangle();

    public EyeSeeGUI() {
        super();
        this.addWindowListener(this);
        this.setResizable(false);
        this.setTitle("Julti EyeSee");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setType(Type.UTILITY);
        this.setAlwaysOnTop(true);
        this.overlay.setAlwaysOnTop(true);

        // set borderless
        this.setVisible(true);
        eyeSeeHwnd = new WinDef.HWND(Native.getWindowPointer(this));
        WindowStateUtil.setHwndBorderless(eyeSeeHwnd);

        tick();
        // 30 = refresh rate
        // TODO: adjustable?
        this.executor.scheduleAtFixedRate(this::tick, 50_000_000, 1_000_000_000L / 30, TimeUnit.NANOSECONDS);
        this.setVisible(false);

//        this.showEyeSee();
    }

    private void tick() {
        if (!currentlyShowing) return;
        if (activeInst == null) return;

        // get snapshot of MC window
        WinDef.HWND sourceHwnd = activeInst.getHwnd();
        Rectangle rectangle = getYoinkArea(sourceHwnd);
        WinDef.HDC sourceHDC = User32.INSTANCE.GetDC(sourceHwnd);
        WinDef.HDC eyeSeeHDC = User32.INSTANCE.GetDC(eyeSeeHwnd);

        // render MC window to EyeSee window
        GDI32Extra.INSTANCE.SetStretchBltMode(eyeSeeHDC, 3);
        GDI32Extra.INSTANCE.StretchBlt(eyeSeeHDC, 0, 0, bounds.width, bounds.height, sourceHDC, rectangle.x, rectangle.y, rectangle.width, rectangle.height, SRCCOPY);

        User32.INSTANCE.ReleaseDC(sourceHwnd, sourceHDC);
        User32.INSTANCE.ReleaseDC(eyeSeeHwnd, eyeSeeHDC);
    }

    public void showEyeSee(Rectangle zoomRect, MinecraftInstance inst) {
        Julti.log(Level.DEBUG, "Showing EyeSee...");
        activeInst = inst;
        currentlyShowing = true;
        this.setVisible(true);
        this.overlay.setVisible(true);

        // TODO: readd manual setting of these values later !
        // credit to priffin for these calculations
        // MonitorUtil.Monitor monitor = MonitorUtil.getPrimaryMonitor();
        // temp fix to incorrect MonitorUtil
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        int projectorWidth = (dm.getWidth() - zoomRect.width) / 2;
        int projectorHeight = (int) (projectorWidth / (16.0f / 9.0f)); // this was weird, just sticking to 16:9

        if (projectorWidth == 0 || projectorHeight == 0) {
            Julti.log(Level.WARN, "Not enough room to create EyeSee window - please decrease your resize width!"); // TODO: "or manually change your EyeSee window settings!"
            return;
        }

        this.bounds.width = projectorWidth;
        this.bounds.height = projectorHeight;

        int projectorXPos = 0;
        int projectorYPos = (dm.getHeight() - projectorHeight) / 2;

        // move eyesee window
        User32.INSTANCE.SetWindowPos(
                eyeSeeHwnd,
                new WinDef.HWND(new Pointer(0)),
                projectorXPos,
                projectorYPos,
                projectorWidth,
                projectorHeight,
                0x0400
        );

        // add overlay image & resize accordingly
        if (!MoveResizePlugin.prevWindowSize.equals(zoomRect)) {
            MoveResizePlugin.prevWindowSize = zoomRect;

            Image image = this.overlay.icon.getImage();
            Image newImage = image.getScaledInstance(projectorWidth, projectorHeight, Image.SCALE_SMOOTH);
            this.overlay.icon = new ImageIcon(newImage);
            this.overlay.label = new JLabel(this.overlay.icon);
            this.overlay.add(this.overlay.label);
        }

        // move overlay window
        User32.INSTANCE.SetWindowPos(
                this.overlay.hwnd,
                new WinDef.HWND(new Pointer(0)),
                projectorXPos,
                projectorYPos,
                projectorWidth,
                projectorHeight,
                0x0400
        );
//        this.overlay.setSize(projectorWidth, projectorHeight);
//        this.overlay.setLocation(projectorXPos, projectorYPos);
//        User32.INSTANCE.BringWindowToTop(this.overlay.hwnd);
    }

    public void hideEyeSee() {
        Julti.log(Level.DEBUG, "Hiding EyeSee...");
        currentlyShowing = false;
        this.setVisible(false);
        this.overlay.setVisible(false);

//        MonitorUtil.Monitor monitor = MonitorUtil.getPrimaryMonitor();
//        User32.INSTANCE.SetWindowPos(
//                eyeSeeHwnd,
//                new WinDef.HWND(new Pointer(0)),
//                0,
//                -monitor.height,
//                1,
//                1,
//                0x0400
//        );

//        this.overlay.setSize(1, 1);
//        this.overlay.setLocation(0, -monitor.height);
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
            WinDef.RECT rect = new WinDef.RECT();
            User32.INSTANCE.GetClientRect(hwnd, rect);
            rectangle = new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
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
        Julti.log(Level.DEBUG, "EyeSee Closed.");
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
