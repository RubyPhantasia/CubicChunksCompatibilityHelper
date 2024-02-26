package com.rubyphantasia.cubic_chunks_compatibility_helper.core;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModInfo;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

public class CubicChunksHelperCoreMod implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        String[] asmTransformerClasses = new String[] {ModInfo.packageName+".config.ConfigEnabledModulesGenerator"};
        return asmTransformerClasses;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
