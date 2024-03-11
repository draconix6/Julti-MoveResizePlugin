package me.draconix6.moveresizeplugin.win32;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.win32.W32APIOptions;

/**
 * JNA interface with Window's gdi32.dll
 * <p>
 * <a href="https://github.com/Lxnus/ScreenCapture4J/blob/master/screencapture4j/GDI32Extra.java">(Source)</a>
 *
 * @Author Lxnus & DuncanRuns
 */
public interface GDI32Extra extends GDI32 {

    GDI32Extra INSTANCE = Native.load("gdi32", GDI32Extra.class, W32APIOptions.DEFAULT_OPTIONS);

    boolean StretchBlt(HDC hdcDest, int xDest, int yDest, int wDest, int hDest, HDC hdcSrc, int xSrc, int ySrc, int wSrc, int hSrc, DWORD rop);

    int SetStretchBltMode(HDC hdc, int mode);

    int SetPixel(HDC hdc, int x, int y, int color);

    HDC CreateCompatibleDC(HDC hdc);
}