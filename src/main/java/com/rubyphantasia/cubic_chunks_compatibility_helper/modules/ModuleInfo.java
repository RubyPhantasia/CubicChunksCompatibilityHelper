package com.rubyphantasia.cubic_chunks_compatibility_helper.modules;

import com.rubyphantasia.cubic_chunks_compatibility_helper.config.ConfigEnabledModules;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

public enum ModuleInfo {
    WORLEYCAVES("worleycaves.WorleyCavesFixModule", "mixins.worleycaves.json",
            Collections.singleton(DependencyMod.WorleyCaves), "Should Worley's Caves be adjusted to also generate below y=0 in Cubic Worlds."),
    PINEAPPLE("", "", Collections.singleton(DependencyMod.Pineapple), false, "Pineapple");
    ;

    public final String moduleClassPath;
    @Nullable
    public final String mixinConfig;
    @Nullable
    public final Set<DependencyMod> requiredMods;

    public final boolean defaultEnabled;
    public final String configDescription;

    ModuleInfo(String moduleClassPath, @Nonnull String mixinConfig, @Nullable Set<DependencyMod> requiredMods,
               boolean defaultEnabled, String configDescription) {
        this.moduleClassPath = moduleClassPath;
        this.mixinConfig = mixinConfig;
        this.requiredMods = requiredMods;
        this.defaultEnabled = defaultEnabled;
        this.configDescription = configDescription;
    }

    ModuleInfo(String moduleClassPath, @Nonnull String mixinConfig,
               @Nullable Set<DependencyMod> requiredMods, String configDescription) {
        this.moduleClassPath = moduleClassPath;
        this.mixinConfig = mixinConfig;
        this.requiredMods = requiredMods;
        this.defaultEnabled = true;
        this.configDescription = configDescription;
    }

    ModuleInfo(String moduleClassPath, @Nullable Set<DependencyMod> requiredMods, String configDescription) {
        this.moduleClassPath = moduleClassPath;
        this.mixinConfig = null;
        this.requiredMods = Collections.unmodifiableSet(requiredMods);
        this.defaultEnabled = true;
        this.configDescription = configDescription;
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

    public boolean shouldLoad() {
        return ConfigEnabledModules.isModuleEnabled(this) && areRequiredModsLoaded();
    }

    public String getLowerName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
