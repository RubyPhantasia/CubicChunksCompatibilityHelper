package com.rubyphantasia.cubic_chunks_compatibility_helper.core;

import net.minecraftforge.fml.common.Loader;

import java.util.HashMap;
import java.util.Map;

public enum MixinConfig {
    ActuallyAdditions("mixins.actuallyadditions.json", new String[] {"actuallyadditions"}),
    RangedPumps("rangedpumps")
//    Test("Mixin_GuiMainMenu", null, Side.CLIENT),
    ;
    public final String mixinConfigFileName;
    private final String[] requiredMods;
    private static final Map<String, MixinConfig> mixinConfigRequiredMods;

    MixinConfig(String mixinClassName, String[] requiredMods) {
        this.mixinConfigFileName = mixinClassName;
        this.requiredMods = requiredMods;
    }

    MixinConfig(String modid) {
        this("mixins."+modid+".json", new String[] {modid});
    }

    private boolean areAllModsLoaded() {
        if (requiredMods != null) {
            for (String modname : requiredMods) {
                if (!Loader.isModLoaded(modname)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean shouldLoad(String configName) {
        MixinConfig config = mixinConfigRequiredMods.get(configName);
        if (config != null) {
            return config.areAllModsLoaded();
        }
        return true;
    }

    static {
        mixinConfigRequiredMods = new HashMap<>();
        for (MixinConfig config : MixinConfig.values()) {
            mixinConfigRequiredMods.put(config.mixinConfigFileName, config);
        }
    }
}
