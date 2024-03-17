### Mixins
* All fields and methods should be prepended with `ccch_` where possible (generally, all methods that don't override a method inherited from Minecraft/Forge/another mod)
* All "added" fields and methods should be annotated with @Unique (fields/methods which don't shadow and aren't @Inject/@Override/etc., and which don't override a method inherited from Minecraft/Forge/another mod)
  * See https://github.com/2xsaiko/mixin-cheatsheet/blob/master/unique.md