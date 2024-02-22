Just a little checklist to help me remember what to do. Up to date as of (Feb. 22, 2024).

### Adding a new mod

* Add gradle dependency (in `dependencies {...}`)
* Obtain deobfuscated mod for use as library (either through supplied dev version or using BON2)
* Place deobfuscated mod in lib/, add as library, and add mod to classpath
  * Would like to automate these steps somehow - a way to auto-download the obfuscated mod from Curse,
    then automagically apply BON2 to it.
* Add copy of the mod to run/mods/
* Add mod as optional dependency in main dependency string

### Adding a new moduleInfo

* Ensure the requisite mods are added as per the above section.
* Decide on a name, `modulename`, for the moduleInfo.
* Add a package `modulename` under `modules` for the moduleInfo-specific code.
* Add an implementation of `IFixModule` to the moduleInfo's package; this should be what directly interacts
  with and sets up any moduleInfo-specific classes/code.
* Add a new enum value to `ModuleEntry` for the new moduleInfo.
* Mixins:
  * If mixins are needed, create `mixins.<modulename>.json`, where `<modulename>` is the moduleInfo's name.
    * If only one is needed, maybe use `modid` for `modulename` where `modid` is the modid in the mod's mcmod.info
* Add mod config name to the `ModuleEntry` enum value for this moduleInfo.
