Plugin for [Julti](https://github.com/duncanruns/julti) which allows for moving & resizing instances.

Usage:
[Download the latest release](https://github.com/draconix6/Julti-MoveResizePlugin/releases/latest), and place it in %UserProfile%/.Julti/plugins. Restart Julti if you had it open.

In Julti, go to Scripts > Import Script. Paste in one of the scrips below, depending on what you're looking to set up:

TallMacro/EyeZoom (More accurate eye measurements): `Tall Window;1;resize 1920 3500`

Pixel Perfect/BoatEye measurements, with a separate magnifier/overlay window & sensitivity change: `Pixel Perfect;1;resize 384 16384 zoom;cursorspeed 1`

Thin BT/TikTok BT: `Thin BT;1;resize 250 700`

Planar fog abuse/WideHardo: `Planar Fog;1;resize 1920 300`

After creating your script, go to Julti Options > Hotkeys. Any hotkeyable scripts will be available at the bottom here:

![image](https://github.com/draconix6/Julti-MoveResizePlugin/assets/30545768/cdc5a5c1-51f6-48f2-894a-212fb4a75c64)

If a resize is active, pressing the same hotkey again will revert the window back to its original position.

**If your window doesn't appear in the expected position when resizing**, go to Julti Options > Window and ensure Window Pos Is Center is checked, and Window Position is set to the center of your monitor.

### Please note, according to speedrun.com/mc rules, only one out of bounds resolution may be used in any given run.

More info on the commands the plugin adds:

- `resize <width> <height> [zoom]` - Resizes the active instance to the given width & height - append `zoom` to the command for a standalone magnifier window to help with eye measurements (i.e. Priffin Mag OBS projector). Example:
![image](https://github.com/draconix6/Julti-MoveResizePlugin/assets/30545768/0f8d03e7-0303-4fc0-8a7a-44166bd0c18e)
- `cursorspeed <speed>` - Changes the Windows cursor speed to the given speed, then back to the original speed when activated again. Can be useful for eye measurements.

Use this general template for importing resize scripts: `Name;1;resize <width> <height> [zoom]`.

Use this general template for importing resize scripts with cursor speed changes: `Name;1;resize <width> <height> [zoom];cursorspeed <speed>`.

More info on Julti scripts:

Julti scripts consist of a name for the script, a number identifying where the script can be run, and a string of commands thereafter, all separated by a semicolon `;`.

The numbers identifying where scripts can be run include:

0 = Can only be run manually through the Scripts menu

1 = Hotkeyable in-game

2 = Hotkeyable on the wall

3 = Hotkeyable either in-game or on the wall

Only resizing the active instance is currently supported, therefore all resize scripts using this plugin should start with `Name;1;`.


A list of commands to use in scripts can be seen by typing `help` in the Julti command line.

Credits to Priffin for TallMacro/EyeZoom, and Duncan for EyeSee.
