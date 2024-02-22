package com.rubyphantasia.cubic_chunks_compatibility_helper.core;

import com.rubyphantasia.cubic_chunks_compatibility_helper.Mod_CubicChunksCompatibilityHelper;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.ModuleManager;
import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

public class LateMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        Mod_CubicChunksCompatibilityHelper.info("Identifying mixins to apply.");
        List<String> mixinsToApply = new ArrayList<>();
        for (MixinConfig mixinConfig : MixinConfig.values()) {
            mixinsToApply.add(mixinConfig.mixinConfigFileName);
        }
        Mod_CubicChunksCompatibilityHelper.info("Proxy: "+Mod_CubicChunksCompatibilityHelper.proxy);
        // This is gross, is there a better way to do this?
        mixinsToApply.addAll(ModuleManager.getModuleMixinConfigs());
        return mixinsToApply;
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        return MixinConfig.shouldLoad(mixinConfig);
    }

}
