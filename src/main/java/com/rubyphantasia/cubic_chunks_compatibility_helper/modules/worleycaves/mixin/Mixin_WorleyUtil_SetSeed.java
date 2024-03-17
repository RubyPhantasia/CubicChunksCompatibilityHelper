package com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves.mixin;

import fluke.worleycaves.util.WorleyUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorleyUtil.class)
public interface Mixin_WorleyUtil_SetSeed {
    @Accessor("m_seed")
    public void ccch_setSeed(int newSeed);
}
