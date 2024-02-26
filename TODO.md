# TODO

## General

* Change Module/ModuleManager so we can handle client/server-specific functionality for modules.
* Some way to automatically test this mod with different runtime mod environments (e.g. automatically
  enabling only specific mods) - at the very least, I should be able to specify the runtime environment/mod set
  in gradle, without having to remove mods or append `.disabled` to their filenames.
* Maybe separate the ModuleManager framework out into a separate library, or add a way for other mods
  add modules to this?
* Add a config file - possibly procedurally generated from a set of config options for each module?
* Some kind of compatibility tracker, that logs whether specific mods are known to be compatible with
  Cubic Chunks, and the degree of that compatibility. Could also note if there are specific variants
  of those mods modified to work with Cubic Chunks.
* Extra handling for the Hybrid world gen mod
  * Github: https://github.com/Barteks2x/HybridWorld
  * Curseforge: https://www.curseforge.com/minecraft/mc-mods/hybrid-world-cc
* Fix up gradle buildscript so it automagically generates fills out the mcmodinfo with the correct information
  * Ideally, it should fill out the modid, mod name, etc. everywhere, even the main mod class, so I only have
    set it in one place.
* Transfer ActuallyAdditions and RangedPumps modules to the new FixModule/ModuleEntry/ModuleManager structure.

## Mod-Specific

### Actually Additions

* Vertical Digger:
  * Make absolute/relative mining depth configurable
  * Display an individual digger's final mining depth
  * Maybe add an option to set an individual digger's stop depth in its GUI?
* Get worldgen working properly with cubic worlds.

### Ranged Pumps

* Make absolute/relative pumping depth configurable.

### Worley's Caves

* Add separate config options that control how it generates in cubic worlds
  * Also, add a config option to control if it uses the Worley's Caves config, or this mod's configs
    where relevant.
* Handle the Hybrid world generator (https://github.com/Barteks2x/HybridWorld) properly.
* Random breaks in the caves - where the tendrils that form them break.
* Ability for Worley's caves nodes (where tendrils join) to connect to Vanilla spaghetti caves.