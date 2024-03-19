## Incompatibility Types

Types of Cubic Chunks incompatibilities I've seen in mods, which this mod can hopefully address.

### World-gen

* World gen methods which use the Vanilla world generator interfaces/base classes, and do not provide cubic equivalents
    * Vanilla's world generation work with chunks, not cubes.
    * Most cases may require writing whole new cubic equivalents

### Other

* Hard-coded y=0, y=256, y=128, checks and assignments etc.
    * For some of these, the logic may also be built around the assumption that y-values cannot be outside the range [0, 256), and use a y-value outside that range as a special flag.
    * Can probably be handled via mixins, but likely not trivial, especially in the case of conditionals that compare against 0, as Java tends to optimize away checks against zero with special instructions (probably not entirely accurate).

* Using BlockPos#toLong() and BlockPos#fromLong()
    * Mostly an issue if the BlockPos instance has a y-value outside the 12-bit range allocated to it in BlockPos#toLong()
    * Ranged Pumps uses this when saving/loading its current position to/from NBT, instead of storing the x, y, and z coordinates separately.
    * Tricky in two ways:
        1. You need to communicate the original y-value somehow in any cases where you're transferring/storing a toLong version of a BlockPos
        2. Once this mod has been added, you can ensure that other mods save the full y-value of a BlockPos where it makes sense, that doesn't help if this mod has been added after a targetted block has saved a long-compressed BlockPos to NBT data - how do you know what y-value that long-compressed BlockPos should have?
        * Or in other words - in the case where a mod saves a long-compressed BlockPos before this helper mod is added, how do you determine what the correct y-value for that long-compressed BlockPos should be?

Should add some way to detect potential issues like these, and log them in some kind of incompatibilities file - to help me find things to improve.
