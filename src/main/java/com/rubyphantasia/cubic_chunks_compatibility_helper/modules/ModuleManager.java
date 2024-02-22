package com.rubyphantasia.cubic_chunks_compatibility_helper.modules;

import com.rubyphantasia.cubic_chunks_compatibility_helper.Mod_CubicChunksCompatibilityHelper;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModuleManager {
    private List<IFixModule> loadedModules = new ArrayList<>();
    private boolean setUp = false;
    private static final String MODULE_MASTER_PACKAGE = Mod_CubicChunksCompatibilityHelper.packageName+".modules";
    public void setupModules() {
        if (!setUp) {
            for (ModuleInfo moduleInfo : ModuleInfo.values()) {
                if (moduleInfo.areRequiredModsLoaded()) {
                    Mod_CubicChunksCompatibilityHelper.info("Attempting to load module "+ moduleInfo +".");
                    try {
                        Class moduleClass = Loader.instance().getModClassLoader().loadClass(MODULE_MASTER_PACKAGE+"."+ moduleInfo.moduleClassPath);
                        IFixModule loadedModule = (IFixModule) moduleClass.newInstance();
                        loadedModule.setupModule();
                        loadedModules.add(loadedModule);
                        Mod_CubicChunksCompatibilityHelper.info("Loaded module "+ moduleInfo +".");
                    } catch (ClassNotFoundException e) {
                        Mod_CubicChunksCompatibilityHelper.warn("Couldn't load the "+ moduleInfo +" module.");
                    } catch (Exception e) {
                        Mod_CubicChunksCompatibilityHelper.warn("Unknown error when trying to instantiate the module class for module "+ moduleInfo +".\n"+e);
                    }
                } else {
                    Mod_CubicChunksCompatibilityHelper.info("Skipping module "+ moduleInfo +".");
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
