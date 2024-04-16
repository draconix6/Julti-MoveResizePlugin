package me.draconix6.moveresizeplugin.win32;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import xyz.duncanruns.julti.win32.User32;

/**
 * @author draconix6, DuncanRuns
 */
public interface User32Extra extends User32 {
    User32Extra INSTANCE = Native.load("user32", User32Extra.class);

    // For giving an integer an IntByReference is not needed
    boolean SystemParametersInfoA(int uiAction, int uiParam, int pvParam, int fWinIni);

    // But it is needed for taking an output
    boolean SystemParametersInfoA(int uiAction, int uiParam, IntByReference pvParam, int fWinIni);
}
