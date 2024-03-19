package com.rubyphantasia.cubic_chunks_compatibility_helper.core.mixin;

import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockPos.class)
public interface Mixin_BlockPos_Accessor {
    @Accessor(value="Y_MASK")
    public static long getY_MASK() {return 0;};

}
