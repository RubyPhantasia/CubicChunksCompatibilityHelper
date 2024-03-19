/*
 * LICENSE:
 *
 * Copyright (C) 2023 RubyPhantasia
 *
 * This file is part of CubicChunksCompatibilityHelper
 *
 * CubicChunksCompatibilityHelper is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.rubyphantasia.cubic_chunks_compatibility_helper.modules;

import com.rubyphantasia.cubic_chunks_compatibility_helper.config.ConfigEnabledModules;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public enum ModuleInfo {
    WORLEYCAVES("worleycaves.FixModuleWorleyCaves", "mixins.worleycaves.json",
            DependencyMod.WorleyCaves, "Should Worley's Caves be patched to also generate below y=0 in Cubic Worlds."),
    ACTUALLY_ADDITIONS_DIGGER("actuallyadditionsdigger.FixModuleActuallyAdditionsDigger", "mixins.actuallyadditionsdigger.json",
            DependencyMod.ActuallyAdditions, "Should Actually Additions' Vertical Digger be patched to mine through y=0."),
    RANGED_PUMPS("rangedpumps.FixModuleRangedPumps", "mixins.rangedpumps.json", DependencyMod.RangedPumps,
                 "Should Ranged Pumps' Pump be patched to pump through y=0."),
    ;

    public final String moduleClassPath;
    @Nullable
    public final String mixinConfig;
    @Nullable
    public final Set<DependencyMod> requiredMods;

    public final boolean defaultEnabled;
    public final String configDescription;

    private static final Map<String, ModuleInfo> mixinReverseMap = new HashMap<>();

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

    ModuleInfo(String moduleClassPath, @Nonnull String mixinConfig,
               @Nullable DependencyMod requiredMod, String configDescription) {
        this.moduleClassPath = moduleClassPath;
        this.mixinConfig = mixinConfig;
        this.requiredMods = Collections.singleton(requiredMod);
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

    public static ModuleInfo lookupModuleByMixinConfig(String mixinConfig) {
        return mixinReverseMap.get(mixinConfig);
    }

    static {
        for (ModuleInfo moduleInfo : values()) {
            mixinReverseMap.put(moduleInfo.mixinConfig, moduleInfo);
        }
    }
}
