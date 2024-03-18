package com.rubyphantasia.cubic_chunks_compatibility_helper.config;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModInfo;
import net.minecraftforge.common.config.Config;

@Config.RequiresWorldRestart
@Config(modid=ModInfo.MODID, name=ModInfo.MODULE_CONFIGS_PATH_PREFIX +"rangedpumps")
public class ConfigRangedPumps {
    @Config.Comment({
            "The deepest a pump can pump down to is its y-level minus maximumRelativeDepth, or deepestPumpableY, whichever is higher in the world.",
            "This variable does nothing."
    })
    public static boolean _explanation = true;

    @Config.Comment("How far a pump can pump below itself.")
    public static int maximumRelativeDepth = 300;

    @Config.Comment("Deepest y-level a pump can pump at.")
    public static int deepestPumpableY = -5000;
}
