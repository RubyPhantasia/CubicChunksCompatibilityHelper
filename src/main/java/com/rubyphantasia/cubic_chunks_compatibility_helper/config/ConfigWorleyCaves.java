package com.rubyphantasia.cubic_chunks_compatibility_helper.config;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModInfo;
import net.minecraftforge.common.config.Config;

@Config.RequiresWorldRestart
@Config(modid= ModInfo.MODID, name=ModInfo.MODULE_CONFIGS_PATH_PREFIX +"worleycaves")
public class ConfigWorleyCaves {

    @Config.Comment("Should we use this config when generating in cubic world generators, prioritizing it over the " +
            "Worley's Cave config where relevant, or only use the Worley's Caves config?")
    public static boolean _useModuleConfig = true; // Underscore prefix so it appears at the top of the config file.

    @Config.Comment("Should we generate down to the bottom of the world, or only down to minGenerationY?")
    public static boolean noMinimumY = true;
    @Config.Comment("Lowest height at which Worley's Caves should generate in a cubic world generator.")
    public static int minCaveY = -5000;
    @Config.Comment("Should we generate up to the top of the world, or only up to maxGenerationY?")
    public static boolean noMaximumY = true;
    @Config.Comment("Maximum height at which Worley's Caves should generate in a cubic world generator.")
    public static int maxCaveY = 64;

    @Config.Comment("How deep lava should be, relative to minCaveY, in a cubic world generator. Lava (or whichever " +
            "block is specified in the Worley's Caves config) will fill spaces from minCaveY to minCaveY+lavaDepth, " +
            "unless.")
    public static int lavaDepth = 100;

    @Config.Comment("Should we use random Perlin noise to randomly distort the Worley caves in a cubic world generator? This is recommended if the " +
            "maxY-minY is very large (or if maxY/minY are disabled), as it increases the variation in the caves.")
    public static boolean useWarpNoise = true;

    @Config.Comment("Should caves become more warped the further down in the world you go? This only applies if noMinimumY " +
            "& noMaximumY are both false. Can be combined with warp noise.")
    public static boolean depthIncreasesCaveWarping = true;
}
