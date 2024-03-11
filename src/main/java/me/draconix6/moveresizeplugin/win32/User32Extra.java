package me.draconix6.moveresizeplugin.win32;

import com.sun.jna.Native;
import xyz.duncanruns.julti.win32.User32;

public interface User32Extra extends User32 {
    User32Extra INSTANCE = Native.load("user32", User32Extra.class);

    boolean SystemParametersInfoA(int uiAction, int uiParam, int pvParam, int fWinIni);
}
