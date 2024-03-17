Adding new modules
* Test that it runs in/out of dev
* Test that its module-specific config generates correctly
  * Occasionally test that the general, moduleEnabled config is generating correctly
* Test that it obeys its module-specific config.
* Test that the new module handles its mod(s) being present/non-present correctly.
  * Occasionally also that it respects its entry in the moduleEnabled setting.
* Test the new module in Vanilla (Cubic Chunks: No/Default) and cubic worlds.
* Test that no other modules broke with it.
* Test that saving and loading doesn't break it somehow.

Transitioning all modules to the new ModuleInfo system:
* [X] Test that it runs in/out of dev
* [X] Test that config generates correctly from scratch
  * [X] Test that config regenerates missing options?
* [X] Test that it handles mods not being present correctly.
* [X] Test that it respects the config options for mods.
* [X] Test that the modules work correctly when loaded.
  * [X] Also test that they do nothing when they are not loaded.
* [X] Test that it works for Vanilla, Vanilla extended (Cubic Chunks: Default when creating a world),
  and cubic world generators.

WorleyCaves Module Config:
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