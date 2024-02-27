package com.rubyphantasia.cubic_chunks_compatibility_helper.modules;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModInfo;
import com.rubyphantasia.cubic_chunks_compatibility_helper.ModLogger;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModuleManager {
    private List<IFixModule> loadedModules = new ArrayList<>();
    private boolean setUp = false;
    private static final String MODULE_MASTER_PACKAGE = ModInfo.packageName+".modules";
    public void setupModules() {
        if (!setUp) {
            for (ModuleInfo moduleInfo : ModuleInfo.values()) {
                if (moduleInfo.areRequiredModsLoaded()) {
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
    public static List<String> getModuleMixinConfigs() {
        return Arrays.stream(ModuleInfo.values())
                .filter(ModuleInfo::areRequiredModsLoaded)
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
}
