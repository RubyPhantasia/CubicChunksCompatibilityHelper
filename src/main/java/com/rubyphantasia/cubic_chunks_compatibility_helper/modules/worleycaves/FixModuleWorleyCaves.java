package com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModLogger;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.IFixModule;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves.event.CubicCaveEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class FixModuleWorleyCaves implements IFixModule {
    public FixModuleWorleyCaves() {}

    @Override
    public void setupModule() {
        ModLogger.info("Hello from WorleyCavesFixModule#setupModule.");
    }

    @Override
    public void preInit() {
        MinecraftForge.TERRAIN_GEN_BUS.register(new CubicCaveEventHandler());
    }
}
