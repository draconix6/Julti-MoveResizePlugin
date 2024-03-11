package me.draconix6.moveresizeplugin.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.win32.StdCallLibrary;

/**
 * JNA interface with Window's user32.dll
 *
 * @author Pete S & DuncanRuns & Lxnus
 */
public interface User32 extends StdCallLibrary {
    User32 INSTANCE = Native.load("user32", User32.class);

    Pointer GetForegroundWindow();

    boolean SetWindowPos(Pointer hwnd, Pointer hwndInsertAfter, int x, int y, int cx, int cy, UINT flags);

    int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);

    boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer userData);

    LONG GetWindowLongA(Pointer hWnd, int nIndex);

    LONG SetWindowLongA(Pointer hWnd, int nIndex, LONG dwNewLong);

    boolean GetClientRect(HWND hWnd, RECT rect);

    HDC GetDC(HWND hWnd);

    BOOL SystemParametersInfoA(int uiAction, int uiParam, int pvParam, int fWinIni);

    interface WNDENUMPROC extends StdCallCallback {
        boolean callback(Pointer hWnd, Pointer arg);
    }

    // This may somehow cause a system exit, removed for safety.
    // int GetKeyNameTextA(LONG lParam, LPSTR lpString, int cchSize);
}