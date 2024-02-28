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
