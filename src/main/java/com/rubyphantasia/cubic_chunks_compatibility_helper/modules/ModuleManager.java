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

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModInfo;
import com.rubyphantasia.cubic_chunks_compatibility_helper.ModLogger;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Loader;

import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager {
    private List<IFixModule> loadedModules = new ArrayList<>();
    private boolean setUp = false;
    private final String MODULE_MASTER_PACKAGE = ModInfo.packageName+".modules";
    private static final ModuleManager instance = new ModuleManager();

    private ModuleManager() {}

    public static ModuleManager getInstance() {
        return instance;
    }

    public void setupModules() {
        if (!setUp) {
            for (ModuleInfo moduleInfo : ModuleInfo.values()) {
                if (moduleInfo.shouldLoad()) {
                    ModLogger.info("Attempting to load module "+ moduleInfo +".");
                    try {
                        Class moduleClass = Loader.instance().getModClassLoader().loadClass(MODULE_MASTER_PACKAGE+"."+ moduleInfo.moduleClassPath);
                        IFixModule loadedModule = (IFixModule) moduleClass.newInstance();
                        loadedModule.setupModule();
                        loadedModules.add(loadedModule);
                        ModLogger.info("Loaded module "+ moduleInfo +".");
                    } catch (ClassNotFoundException e) {
                        ModLogger.warn("Couldn't load the "+ moduleInfo +" module.");
                    } catch (Exception e) {
                        ModLogger.warn("Unknown error when trying to instantiate the module class for module "+ moduleInfo +".\n"+e);
                    }
                } else {
                    ModLogger.info("Skipping module "+ moduleInfo +".");
                }
            }
            setUp = true;
        } else {
            throw new RuntimeException("Attempted to set up modules a second time.");
        }
    }

    /**
     * Called by LateMixinLoader#getMixinConfigs; is there a better way to handle this? Maybe integrating ModuleManager into LateMixinLoader?
     * @return
     */
    public List<String> getModuleMixinConfigs() {
        // Manually synchronize the config, as configs have not been synchronized w/ the disk yet when ILateMixinLoader is calling this.
        ConfigManager.sync(ModInfo.MODID, Config.Type.INSTANCE);
        return Arrays.stream(ModuleInfo.values())
                .filter(ModuleInfo::shouldLoad)
                .map(moduleInfo -> moduleInfo.mixinConfig)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void runModulesPreInit() {
        if (!setUp) {
            throw new RuntimeException("ModuleManager#runModulesPreInit was called before modules were set up.");
        } else {
            for (IFixModule loadedModule : loadedModules) {
                loadedModule.preInit();
            }
        }
    }

    public boolean shouldMixinConfigLoad(String mixinConfig) {
        ConfigManager.sync(ModInfo.MODID, Config.Type.INSTANCE);
        return ModuleInfo.lookupModuleByMixinConfig(mixinConfig).shouldLoad();
    }
}
