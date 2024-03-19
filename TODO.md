# TODO

## General

* Change Module/ModuleManager so we can handle client/server-specific functionality for modules.
* Some way to automatically test this mod with different runtime mod environments (e.g. automatically
  enabling only specific mods) - at the very least, I should be able to specify the runtime environment/mod set
  in gradle, without having to remove mods or append `.disabled` to their filenames.
* Maybe separate the ModuleManager framework out into a separate library, or add a way for other mods
  add modules to this?
* [DONE] Add a config file - possibly procedurally generated from a set of config options for each module?
* Some kind of compatibility tracker, that logs whether specific mods are known to be compatible with
  Cubic Chunks, and the degree of that compatibility. Could also note if there are specific variants
  of those mods modified to work with Cubic Chunks.
* Extra handling for the Hybrid world gen mod
  * Github: https://github.com/Barteks2x/HybridWorld
  * Curseforge: https://www.curseforge.com/minecraft/mc-mods/hybrid-world-cc
* Fix up gradle buildscript so it automagically generates fills out the mcmodinfo with the correct information
  * Ideally, it should fill out the modid, mod name, etc. everywhere, even the main mod class, so I only have
    set it in one place.
* [DONE] Transfer ActuallyAdditions and RangedPumps modules to the new FixModule/ModuleEntry/ModuleManager structure.
* Update dev environment setup instructions with my experiences on Kubuntu.
* Update my AddingNewMods.md?
* Move dev environment setup instructions into a separate file.
* Link to all the relevant files from my README.md
* [DONE] Add TESTING.md
* Maybe move all my info files (Credits.md, AddingNewMods.md, etc.), except for a few major ones (e.g. README.md), to
  a folder?
* See about using LangKeys for the config comments?
* Go through and add comments where needed.
* Note in the config file that to disable a module, they need to type "false" _exactly_ - any other string
  will count as "true", even "fals".
  * See if there's a way to detect when the text entered for a boolean isn't "true"/"false"
* Add the `RequiresMcRestart` annotation to entries in the main _enabledModules_ config.
* Suppress warnings where appropriate
  * "Unused field/method" warning for certain members (such as _explanation field in config for Actually Additions Digger module)
  * "public is redundant for interface members"
* Determine if the roundabout (I_SomeMixinInterface ((Object) targetInstance)) cast is necessary for using interfaces which are added to mixin-targeted classes, and suppress the warnings if necessary.
* Change mixins to use ModifyExpressionValue and other MixinExtras injectors where needed.
* Add list of common Cubic Chunks incompatibilities I've seen.
* Add way for my mod to automatically detect common incompatibilities in other mods and record them in a file.
* Add license text to header of all java files.
* Get in-game config menu working for my mod
* Get the mod info to display correctly in-game
* Add tasks to clean up the run directory (remove old saves, log files, crash reports, and configs)
  * Or check if ForgeGradle already provides tasks like these
* See about creating some way to automatically test modules, where possible
  * I.e. loading into a world with specific config settings, placing blocks, waiting X ticks, then checking that certain blocks are at specified positions.

## Mod-Specific

### Actually Additions

* Vertical Digger:
  * [DONE] Make absolute/relative mining depth configurable
  * Display an individual digger's final mining depth
  * Maybe add an option to set an individual digger's stop depth in its GUI?
  * Add a maxDiggableY config option
  * Maybe add verification that ensures the digger can't dig down to its NOT_STARTED_Y_VALUE, Integer.MAX_VALUE?
* Get worldgen working properly with cubic worlds.

### Ranged Pumps

* [DONE] Make absolute/relative pumping depth configurable.
* Some kind of option to set an individual pump's max depth in its GUI?
* Maybe fix an apparent bug in the TilePump::onLoad method, where it always calls rebuildSurfaces, even if the pump has already exceeded its range? This might just be overwritten when readFromNBT is called.
* Maybe detect if the pump's configured range has increased since it was last saved, and ensure that it pumps out the newly accessible parts of any columns it previously pumped (i.e. ensure that it pumps all columns down to the new range limit).
* Ensure pump correctly saves & restores its progress.

### Worley's Caves

* [DONE] Add separate config options that control how it generates in cubic worlds
  * [DONE] Also, add a config option to control if it uses the Worley's Caves config, or this mod's configs
    where relevant.
* Handle the Hybrid world generator (https://github.com/Barteks2x/HybridWorld) properly.
* Random breaks in the caves - where the tendrils that form them break.
* Ability for Worley's caves nodes (where tendrils join) to connect to Vanilla spaghetti caves.
* Ability to have layers where Worley's Caves generates?
* See why the caves seem to roughly generate in two-block-high steps.
* Note that if its config doesn't mention a particular setting, it uses the corresponding setting in
  worleycaves.cfg
* [DONE] Add the ability to increase cave warp with descending y-level?
  * [DONE] Maybe config options allowing control over how cave warp increases, and how that combines with
    noise-based warp if enabled. 