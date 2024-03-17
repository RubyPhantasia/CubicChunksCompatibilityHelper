package com.rubyphantasia.cubic_chunks_compatibility_helper.config;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModInfo;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.actuallyadditionsdigger.constants.Constants_Miner_Deeper;
import net.minecraftforge.common.config.Config;

@SuppressWarnings("CanBeFinal")
@Config.RequiresWorldRestart
@Config(modid=ModInfo.MODID, name=ModInfo.MODULE_CONFIGS_PATH_PREFIX +"actuallyadditions_digger")
public class ConfigActuallyAdditionsDigger {
    @Config.Comment({
            "The deepest a digger can dig down to is its y-level minus maximumRelativeDepth, or deepestDiggableY, whichever is greater.",
            "This variable does nothing."
    })
    public static boolean _explanation=true;
    @Config.Comment("How far the digger can dig below itself.")
    public static int maximumRelativeDepth = 300;
    @Config.Comment("Deepest y-level the digger can dig down to. The digger cannot dig below this y-level, even if its " +
            "maximumRelativeDepth would otherwise permit it.")
    @Config.RangeInt(min=Constants_Miner_Deeper.NOT_STARTED_Y_VALUE+1)
    public static int deepestDiggableY = -5000;
}
