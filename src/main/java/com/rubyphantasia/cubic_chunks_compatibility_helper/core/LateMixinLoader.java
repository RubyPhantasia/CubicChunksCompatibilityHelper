package com.rubyphantasia.cubic_chunks_compatibility_helper.core;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModLogger;
import com.rubyphantasia.cubic_chunks_compatibility_helper.Mod_CubicChunksCompatibilityHelper;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.ModuleManager;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

public class LateMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        ModLogger.info("Identifying mixins to apply.");
        List<String> mixinsToApply = new ArrayList<>();
        // This is gross, is there a better way to do this?
        mixinsToApply.addAll(ModuleManager.getInstance().getModuleMixinConfigs());
        return mixinsToApply;
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        return ModuleManager.getInstance().shouldMixinConfigLoad(mixinConfig);
    }

}
