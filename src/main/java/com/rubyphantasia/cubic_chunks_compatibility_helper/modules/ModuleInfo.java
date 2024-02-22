package com.rubyphantasia.cubic_chunks_compatibility_helper.modules;

import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

public enum ModuleInfo {
    WORLEYCAVES("worleycaves.WorleyCavesFixModule", "mixins.worleycaves.json", Collections.singleton(DependencyMod.WorleyCaves))
    ;

    public final String moduleClassPath;
    @Nullable
    public final String mixinConfig;
    @Nullable
    public final Set<DependencyMod> requiredMods;

    ModuleInfo(String moduleClassPath, @Nonnull String mixinConfig, @Nullable Set<DependencyMod> requiredMods) {
        this.moduleClassPath = moduleClassPath;
        this.mixinConfig = mixinConfig;
        this.requiredMods = requiredMods;
    }

    ModuleInfo(String moduleClassPath, @Nullable Set<DependencyMod> requiredMods) {
        this.moduleClassPath = moduleClassPath;
        this.mixinConfig = null;
        this.requiredMods = Collections.unmodifiableSet(requiredMods);
    }

    public boolean areRequiredModsLoaded() {
        if (requiredMods != null) {
            for (DependencyMod mod : requiredMods) {
                if (!Loader.isModLoaded(mod.modid)) {
                    return false;
                }
            }
        }
        return true;
    }
}
