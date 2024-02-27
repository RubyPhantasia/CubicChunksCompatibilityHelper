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