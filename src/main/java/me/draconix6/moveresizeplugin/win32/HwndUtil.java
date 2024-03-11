package me.draconix6.moveresizeplugin.win32;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.LONG;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author DuncanRuns
 */
public final class HwndUtil {
    private HwndUtil() {
    }

    public static String getHwndTitle(Pointer hwnd) {
        byte[] x = new byte[128];
        User32.INSTANCE.GetWindowTextA(hwnd, x, 128);
        StringBuilder out = new StringBuilder();
        for (byte a : x) {
            if (a == 0)
                break;
            out.append((char) a);
        }
        return out.toString();
    }

    // Sets a window to be borderless but does not move it.
    public static void setHwndBorderless(Pointer hwnd) {
        long style = getHwndStyle(hwnd);
        style &= ~(Win32Con.WS_BORDER
                | Win32Con.WS_DLGFRAME
                | Win32Con.WS_THICKFRAME
                | Win32Con.WS_MINIMIZEBOX
                | Win32Con.WS_MAXIMIZEBOX
                | Win32Con.WS_SYSMENU);
        setHwndStyle(hwnd, style);
    }

    public static long getHwndStyle(Pointer hwnd) {
        return User32.INSTANCE.GetWindowLongA(hwnd, Win32Con.GWL_STYLE).longValue();
    }

    public static void setHwndStyle(Pointer hwnd, long style) {
        User32.INSTANCE.SetWindowLongA(hwnd, Win32Con.GWL_STYLE, new LONG(style));
    }

    public static Rectangle getHwndInnerRectangle(Pointer hwnd) {
        WinDef.RECT rect = new WinDef.RECT();
        User32.INSTANCE.GetClientRect(new WinDef.HWND(hwnd), rect);
        return new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
    }

    /**
     * Waits for a window with an exact name. Once the window appears, the window handle is returned.
     * <p>
     * DO NOT use this method unless you are certain that the window will be present.
     *
     * @param exactName the exact name of the window
     *
     * @return the window handle
     */
    public static Pointer waitForWindow(String exactName) {
        AtomicReference<Pointer> out = new AtomicReference<>(null);
        while (out.get() == null) {
            try {
                Thread.sleep(1000 / 20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            out.set(getExactWindow(exactName));
        }
        return out.get();
    }

    public static Pointer getExactWindow(String exactName) {
        AtomicReference<Pointer> out = new AtomicReference<>(null);
        User32.INSTANCE.EnumWindows((hWnd, arg) -> {
            if (HwndUtil.getHwndTitle(hWnd).equals(exactName)) {
                out.set(hWnd);
                return false;
            }
            return true;
        }, null);
        return out.get();
    }
}
