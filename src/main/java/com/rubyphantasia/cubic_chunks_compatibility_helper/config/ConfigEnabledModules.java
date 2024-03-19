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

package com.rubyphantasia.cubic_chunks_compatibility_helper.config;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModInfo;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.ModuleInfo;
import net.minecraftforge.common.config.Config;

import java.util.HashSet;
import java.util.Set;

@Config(modid=ModInfo.MODID, name = ModInfo.MODID+"_enabledModules")
public class ConfigEnabledModules {

    private static final Set<String> enabledModulesFieldNames = new HashSet<>();

    public static boolean isModuleEnabled(ModuleInfo moduleInfo) {
        String moduleEnabledFieldName = ConfigEnabledModulesGenerator.getModuleEnabledFieldName(moduleInfo);
        if (enabledModulesFieldNames.contains(moduleEnabledFieldName)) {
            try {
                return ConfigEnabledModules.class.getField(moduleEnabledFieldName).getBoolean(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Couldn't access the moduleEnabled config entry for module: "+moduleInfo.name());
            }
        } else {
            throw new RuntimeException("No moduleEnabled field for module: "+moduleInfo.name());
        }
    }

    static {
        for (ModuleInfo moduleInfo : ModuleInfo.values()) {
            enabledModulesFieldNames.add(ConfigEnabledModulesGenerator.getModuleEnabledFieldName(moduleInfo));
        }
    }
}
