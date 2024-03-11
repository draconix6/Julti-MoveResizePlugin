Plugin for [Julti](https://github.com/duncanruns/julti) which allows for moving & resizing instances.

Usage:
Download the latest release, and place it in %UserProfile%/.Julti/plugins. Restart Julti if you had it open.

In Julti, go to Scripts > Import Script. The plugin currently implements two commands:

- `resize <width> <height> [zoom]` - Resizes the active instance to the given width & height - append `zoom` to the command for a magnifier window to help with eye measurements (i.e. Priffin Mag OBS projector). Example:
![image](https://github.com/draconix6/Julti-MoveResizePlugin/assets/30545768/0f8d03e7-0303-4fc0-8a7a-44166bd0c18e)
- `cursorspeed <speed1> <speed2>` - Changes the Windows cursor speed to speed1, then back to speed2 when activated again. Can be useful for eye measurements.


Use this general template for importing resize scripts: `Name;1;resize <width> <height> [zoom]`.

Use this general template for importing resize scripts with cursor speed changes: `Name;1;resize <width> <height> [zoom];cursorspeed <speed1> <speed2>`.

After creating your script, go to Julti Options > Hotkeys. Any hotkeyable scripts will be available at the bottom here:
![image](https://github.com/draconix6/Julti-MoveResizePlugin/assets/30545768/cdc5a5c1-51f6-48f2-894a-212fb4a75c64)

An example script to resize the game for more accurate eye measurements: `Tall Window;1;resize 1920 3500`

An example script to resize the game for pixel perfect (boat eye) measurements: `Pixel Perfect;1;resize 384 16384 zoom`

An example script to resize the game for thin BT/tiktok BT: `Thin BT;1;resize 250 700`

An example script to resize the game for planar fog abuse: `Planar Fog;1;resize 1920 300`

If a resize is active, pressing the same hotkey again will revert the window back to its original position.

### Please note, according to speedrun.com/mc rules, only one out of bounds resolution may be used in any given run.

If your window doesn't appear in the expected position when resizing, go to Julti Options > Window and ensure Window Pos Is Center is checked, and Window Position is set to the center of your monitor.

More info on Julti scripts:

Julti scripts consist of a name for the script, a number identifying where the script can be run, and a string of commands thereafter, all separated by a semicolon `;`.

The numbers identifying where scripts can be run include:

0 = Can only be run manually through the Scripts menu

1 = Hotkeyable in-game

2 = Hotkeyable on the wall

3 = Hotkeyable either in-game or on the wall

Only resizing the active instance is currently supported, therefore all resize scripts using this plugin should start with `Name;1;`.


A list of commands to use in scripts can be seen by typing `help` in the Julti command line.

Credits to Priffin for TallMacro/EyeZoom.
