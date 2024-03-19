### Adding new modules
* Test that it runs in/out of dev
* Test that its module-specific config generates correctly
  * Occasionally test that the general, moduleEnabled config is generating correctly
* Test that it obeys its module-specific config.
* Test that the new module handles its mod(s) being present/non-present correctly.
  * Occasionally also that it respects its entry in the moduleEnabled setting.
* Test the new module in Vanilla (Cubic Chunks: No/Default) and cubic worlds.
* Test that no other modules broke with it.
* Test that saving and loading doesn't break it somehow.

### Transitioning all modules to the new ModuleInfo system:
* [X] Test that it runs in/out of dev
* [X] Test that config generates correctly from scratch
  * [X] Test that config regenerates missing options?
* [X] Test that it handles mods not being present correctly.
* [X] Test that it respects the config options for mods.
* [X] Test that the modules work correctly when loaded.
  * [X] Also test that they do nothing when they are not loaded.
* [X] Test that it works for Vanilla, Vanilla extended (Cubic Chunks: Default when creating a world),
  and cubic world generators.

### WorleyCaves Module Config:
* That CubicWorleyCaveGenerator::maxCaveY and other variables are set correctly based on the
  config, for several cases
  * Cases: _useModuleConfg true/false, noMaximumY/noMinimumY = true/false, lavaDepth, etc.
  * [X] Case: default values
  * [X] Case: default, w/ _useModuleConfig set to false
  * [X] Case: default, w/ noMaximumY and noMinimumY set to false, and lavaDepth, minCaveY, and
    maxCaveY set to non-default values.
  * [X] Case: default, w/ noMaximumY set to false, and lavaDepth/minCaveY/maxCaveY set to
    non-default values.
  * [X] Case: default, w/ noMinimumY set to false, and lavaDepth/minCaveY/maxCaveY set to
    non-default values.
  * [X] Case: default, w/ noMaximumY and noMinimumY set to false, and lavaDepth, minCaveY, and
    maxCaveY set to non-default values, and useWarpNoise set to false.
  * [X] Case: default, w/ noMaximumY and noMinimumY set to false, and lavaDepth, minCaveY, and
    maxCaveY set to non-default values, and depthIncreasesCaveWarping set to false.
* That the mod is able to correctly use a block other than the default lava for LAVA.
* That the caves are correctly smoothed at minCaveY & maxCaveY
* That it correctly fills caves up to lavaDepth if noMinimumY is false.
* That my debug visualizer is turned on/off correctly.
* That the depth-dependent warping works, w/o noise-based warping
* That the noise-based warping works, w/o depth-dependent warping
* That the two types of warping work together
* That you can turn off both kinds of warping
* That we don't see flaring at maxCaveY
* [X] Case: default values, check that caves continue generating past the specified min/maxCaveY
  (respecting the "noMin/noMax..." settings), and that noise warping seems to apply
* [X] Case: default values, w/ noise-based warping disabled, check that caves continue generating past the specified min/maxCaveY
  (respecting the "noMin/noMax..." settings), and that no warping is happening
* [X] Case: noMax/noMin set to false, non-default lavaDepth and min/max caveY, no warping
* [X] Case: noMax/noMin set to false, non-default lavaDepth and min/max caveY, no noise warping
* [X] Case: noMax/noMin set to false, non-default lavaDepth and min/max caveY, all warping
* [X] Case: noMax/noMin set to false, non-default lavaDepth and min/max caveY, all warping, different block for lava

### Actually Additions Digger
* [X] Case: deepestDiggableY = -4000
  * [X] Subcase: Miner at -3995, does not dig below deepestDiggableY
  * [X] Subcase: Miner at -3999, does not dig below deepestDiggableY
  * [X] Subcase: Miner at -4000, does not dig at all
  * [X] Subcase: Miner at -4005, does not dig at all
  * [X] Subcase: Miner at -10000, does not dig at all
  * [X] Subcase: Miner at -1, digs down
  * [X] Subcase: Miner at 0, digs down
  * [X] Subcase: Miner at 1, digs down across y=0
  * [X] Subcase: Miner at 2, digs down across y=0
* [X] Case: deepestDiggableY = -4000, maximumRelativeDepth = 5
  * [X] Subcase: Miner at -3994, digs down five blocks
  * [X] Subcase: Miner at -3995, digs down five blocks
  * [X] Subcase: Miner at -3996, digs down four blocks
  * [X] Subcase: Miner at -3999, digs down one block
  * [X] Subcase: Miner at -4000, does not dig down at all
  * [X] Subcase: Miner at -4001, does not dig down at all
  * [X] Subcase: Miner at 2, digs down five blocks
  * [X] Subcase: Miner at 1, digs down five blocks
  * [X] Subcase: Miner at 0, digs down five blocks
  * [X] Subcase: Miner at -1, digs down five blocks
    * [X] Subcase: Miner resumes mining if maximumRelativeDepth is upped to 20
* [X] Case: deepestDiggableY = -4000, maximumRelativeDepth = 20
  * [X] Subcase: Miner at -3970, digs down twenty blocks
    * [X] Subcase: Miner stops mining if maximumRelativeDepth is dropped to 10 after it's dug over 10 blocks
    * [X] Subcase: Miner stops mining early if deepestDiggableY is changed to -3000 after it's dug a few blocks
  * [X] Subcase: Miner at -3990, digs down ten blocks
    * [X] Subcase: Miner continues mining if deepestDiggableY is changed to -5000

### Worley's Caves, changing m_seed Accessor mixin
* [X] Case: World seed 7, World type Default; WorleyUtil seed should be 1337
* [X] Case: World seed 7, World type CWG; WorleyUtil seed should be -1156638823
* [X] Case: World seed 42, World type CWG; WorleyUtil seed should be -1170105035
* Want to ensure nothing has changed.

### Ranged Pumps

#### General Pump Tests

* [X] Case: deepestPumpableY = -4000
  * [X] Subcase: Pump at 2, pumps down across y=0
  * [X] Subcase: Pump at 1, pumps down across y=0
  * [X] Subcase: Pump at 0, pumps down
  * [X] Subcase: Pump at -3999, does not pump below deepestPumpableY
  * [X] Subcase: Pump at -4000, does not pump at all
  * [X] Subcase: Pump at -4005, does not pump at all
* [X] Case: deepestPumpableY = -4000, maximumRelativeDepth = 5
  * [X] Subcase: Pump at -3994, pumps down five blocks
  * [X] Subcase: Pump at -3995, pumps down five blocks
  * [X] Subcase: Pump at -3996, pumps down four blocks
  * [X] Subcase: Pump at -4000, does not pump down at all
  * [X] Subcase: Pump at -4001, does not pump down at all
  * [X] Subcase: Pump at 2, pumps down five blocks
    * [X] Subcase: Pump starts pumping deeper if maximumRelativeDepth is upped to 10
* [X] Case: deepestPumpableY = -4000, maximumRelativeDepth = 20
  * [X] Subcase: Pump at -3970, pumps down twenty blocks
    * [X] Subcase: Pump continues pumping at the correct height if the world is saved and reloaded
    * [X] Subcase: Pump continues pumping at roughly the correct height after all four of these steps have been executed:
      1. Saving the world
      2. Loading the world without the module enabled
      3. Saving the world again
      4. Loading the world with the module enabled
      * Note: used maximumRelativeDepth = 5, pump at -3000 for this test
  * [X] Subcase: Pump at -3990, pumps down ten blocks
    * [X] Subcase: Pump starts pumping deeper if deepestPumpableY is changed to -5000
  * [X] Subcase: Pump at 0, 2 in non-cubic world; doesn't pump below bedrock.

#### Save-data
* Goals:
  * When loading a world which didn't previous have this module enabled, it correctly guesses the actual BlockPos's for current and future positions (surfaces).
  * Correctly saves and reloads full, untruncated position for pumps
* Tests:
  * [X] Save world with pumps at -2400, -4, 4, and 3000, without this module enabled; when loading the world with this module enabled, it should correctly determine each of their current/future positions
    * [X] Pump at 4 should be allowed to pump long enough to generate a list of surfaces.
  * [X] Save world with pump at 71, with currentPos down at -5001, without this module enabled; should not correctly guess the position (should wrap to about y=96)
  * [X] Save world with Pumps at -2400, 4, 4, and 3000, with this module enabled; when loading the world, should correctly guess the position
  * [X] Save world with two pumps at -4, one with the module enabled, one without the module enabled; when loading world with module enabled, it should correctly determine each of their current/future positions
  * [X] Save world with one pump at -3000, with this module enabled; when loading the world without the module, should not guess position correctly.
* A method that works is to create all the pumps, then save the world w/ breakpoints enabled (and a breakpoint at the end of the TilePump::writeToNBT method), capture the relevant state as a single string (e.g. ""+this.currentPos+this.surfaces.toString()+tag), and copy it into a text editor; repeat this for each pump as it saves to disk. Then, when loading the world, enable the appropriate readFromNBT breakpoint(s), capture the relevant state at the end of the method, and use Python to compare the two strings - they should be equal.

#### Fixing pump attempting to pump below world height
* Added check for world height to the ccch_setDoneIfBelowPumpingYLimit function; refactored common functionality out of it and the allowPumpingAcrossYZero function.
* Tests in cubic world:
  * [X] Pumps at 2, 0, and -1, maximumRelativeDepth=5; should pump across y=0
  * [X] Pump at -3999, deepestPumpableY=-4000; should only pump down to -4000
  * [X] Pump at -4000, -4001, deepestPumpableY=-4000; should not pump at all
* Test in non-cubic world:
  * [X] Pump at 1, deepestPumpableY=-4000; should only pump down to y=0
  * [X] Pump at 0, deepestPumpableY=-4000; should not pump at all
  * [X] Pump at 1, deepestPumpableY=0; should only pump down to y=0
  * [X] Pump at 0, deepestPumpableY=0; should not pump at all
  * [X] Pump at 2, deepestPumpableY=1; should only pump down to y=1
  * [X] Pump at 1, 0, deepestPumpableY=1; should not pump at all