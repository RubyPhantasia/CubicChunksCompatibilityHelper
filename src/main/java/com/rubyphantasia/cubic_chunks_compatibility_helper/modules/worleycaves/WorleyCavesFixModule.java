package com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves;

import com.rubyphantasia.cubic_chunks_compatibility_helper.Mod_CubicChunksCompatibilityHelper;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.IFixModule;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves.event.CubicCaveEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class WorleyCavesFixModule implements IFixModule {
    public WorleyCavesFixModule() {}

    @Override
    public void setupModule() {
        Mod_CubicChunksCompatibilityHelper.info("Hello from WorleyCavesFixModule#setupModule.");
    }

    @Override
    public void preInit() {
        MinecraftForge.TERRAIN_GEN_BUS.register(new CubicCaveEventHandler());
    }
}
