## Cubic Chunks Compatibility Helper Mod

A mod that tweaks other mods to make them what I call "properly compatible" with Cubic Chunks 1.12.2 - so
they not only work without crashing, but work as one might reasonably expect in a Cubic Chunks world.
For example, this mod makes the Actually Additions Miner mine through y=0, down to a (to be) configurable
depth relative to the miner. And it modifies Worley's Caves to continue generating below y=0, if a
cubic world generator is used (e.g. [Cubic World Generator](https://github.com/OpenCubicChunks/CubicWorldGen)).
This mod is in a pretty early state, and while the features I've implemented should be fairly well-tested,
they are not always complete, or fully configurable.

### Setting up this mod in Dev (INCOMPLETE)

This guide is written for Intellij IDEA, as that is what I use (Community Edition version 2021.2.1, to be exact); if someone could provide equivalent steps
  for Eclipse or other IDEs, that would be appreciated.

To set this mod up, you should just need to download/clone the repo, then import the repo's folder into IntelliJ. and config a couple things in Intellij.
E.g.:
1. Download the .zip from GitHub
2. Extract the .zip, and rename the resultant folder whatever you want
3. Open up IntelliJ and tell it to open the folder as a project
4. Wait for Intellij to finish setting up.
5. Run the genIntellijRuns task (gradle tab, under Tasks/forgegradleruns)
6. Set Intellij to use IntellijIDEA for runs
    a. Gradle tab, Build Tool Settings (wrench icon)
    b. Gradle Settings
    c. Build and run using "Intellij IDEA"
    d. Run tests using "Intellij IDEA"
7. Set the project's output directory to /out/
    a. File>Project Structure
    b. Project compiler output: Path to your mod's folder, plus "\out"
        * Should be the full, qualified path, as it would appear in Windows Explorer (or your OS's file explorer)
        * E.g. "D:\Documents\TestMod\out"
        * You may need to explicitly create the "out" folder.
        * Project compiler output is under Project Settings>Project
    c. Potentially restart your IDE.
8. Add the CubicChunks dev-all jar to the run/mods directory
    * Can't figure out how to avoid needing this.
    * .jar is currently available at: https://jenkins.daporkchop.net/job/OpenCubicChunks/job/CubicChunks/job/MC_1.12/ under the "Last Successful Artifacts".
        * Filename's format is: "CubicChunks-1.12-"+versionNumber+"-SNAPSHOT-dev-all.jar"
    * Again, you may need to explicitly create the run/mods directory.
9. For each extra mod in the main dependency string (in the `@Mod` annotation on the main mod
   class, `Mod_CubicChunksCompatabilityHelper`)
   1. Obtain a copy of that mod (preferably the same version specified in the lower bound of the
      version range for that mod)
   2. Place a deobfuscated copy of the mod in the `lib/` folder.
      * If a mod does not have a deobfuscated version available, you can use Bearded-Octo-Nemesis 2 (BON2, available at https://github.com/Valiec/BON2)
      * From what I recall, you will need to clone the repo and build it (via the
        command `gradle build`, I think?), then use the `build/libs/BON-2.4.0.CUSTOM-all.jar` for
        deobfuscating mods.
   3. Place the original copy of the mod in the `runs/mods/` folder.
      * This is not required unless the mod (otherMod) is required in the dependency string; this is mostly if you want
        to see how this mod (compatMod) operates with the otherMod in its runtime environment.

#### Notes:

* For running the project, you need to use the IntelliJ runClient/runServer configurations (in the
  dropdown next to the run/debug buttons on a fresh install of Intellij IDEA).
* While not required, I recommend adding a copy of the CubicWorldGen mod (or another Cubic worldgen mod)
  to the `run/mods/` folder, if you are interested in seeing (or changing) how the other mods' compatibilized
  worldgen works in a cubic world.
  * [Cubic World Generator Github](https://github.com/OpenCubicChunks/CubicWorldGen)
  * The latest compiled copy of CubicWorldGen can be found on the Cubic Chunks discord server, or at https://jenkins.daporkchop.net/job/OpenCubicChunks/job/CubicWorldGen/job/MC_1.12/
* A link to the Cubic Chunks discord server can be found at the bottom of the Cubic Chunks mod page, https://modrinth.com/mod/cubicchunks
* This is based on the Mixins_and_bufixes branch of the CubicChunks-Template-Mod
  * Github: https://github.com/Joekeen03/CubicChunks-Template-Mod/tree/Mixins_and_bufixes 