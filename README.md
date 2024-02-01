Plugin for [Julti](https://github.com/duncanruns/julti) which allows for moving & resizing instances. Currently, only resizing the current instance through scripts is supported, however moving and resizing other windows is planned.

Usage:
Download the latest release, and place it in %UserProfile%/.Julti/plugins. Restart Julti if you had it open.

In Julti, go to Scripts > Import Script. The plugin currently implements just one command, `resize <width> <height>` - at the moment, any resizing scripts should start with `Name;1;resize <width> <height>`.

After creating your script, go to Julti Options > Hotkeys. Any hotkeyable scripts will be available at the bottom here.

An example script to resize the game for more accurate eye measurements:

`Tall Window;1;resize 1920 3500`

An example script to resize the game for pixel perfect (boat eye) measurements:

`Pixel Perfect;1;resize 1920 16384`

An example script to resize the game for thin BT/tiktok BT:

`Thin BT;1;resize 250 700`

An example script to resize the game for planar fog abuse:

`Planar Fog;1;resize 1920 300`

If a resize is active, pressing the same hotkey will revert the window back to its original position.

### Please note, according to speedrun.com/mc rules, only one out of bounds resolution may be used in any given run.

More info on Julti scripts:

Julti scripts consist of a name for the script, a number identifying where the script can be run, and a string of commands thereafter, all separated by a semicolon `;`.

The numbers identifying where scripts can be run include:

0 = Can only be run manually through the Scripts menu

1 = Hotkeyable in-game

2 = Hotkeyable on the wall

3 = Hotkeyable either in-game or on the wall

Only resizing the active instance is currently supported (i.e. 1).


A list of commands to use in scripts can be seen by typing `help` in the Julti command line.

Credits to Priffin for TallMacro/EyeZoom.
