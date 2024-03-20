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
import net.minecraftforge.common.config.Config;

@Config.RequiresWorldRestart
@Config(modid= ModInfo.MODID, name=ModInfo.MODULE_CONFIGS_PATH_PREFIX +"worleycaves")
public class ConfigWorleyCaves {

    @SuppressWarnings("unused")
    @Config.Comment({
            "This config controls how Worley's Caves generates in cubic world generators/types.",
            "Note that a cubic world type/generator is not the same thing as a cubic world; the",
            "\tCubicChunks: No/Default option when generating a world only controls if that",
            "\tworld is cubic - if players can build outside the Vanilla build range. A cubic",
            "\tworld type/generator is a world generator that has specifically been designed to",
            "\tgenerate cubic worlds, and the Cubic Chunks mod alone does not provide any such",
            "\tgenerator.",
            "This variable does nothing"
    })
    public static boolean _explanation = true;

    @Config.Comment({
            "Should we use this config when generating in cubic world generators, prioritizing",
            "\tit over the Worley's Cave config where relevant, or only use the Worley's",
            "\tCaves config for all parameters?"
    })
    public static boolean _useModuleConfig = true; // Underscore prefix so it appears at the top of the config file.

    @Config.Comment("Should we generate down to the bottom of the world, or only down to minGenerationY?")
    public static boolean noMinimumY = false;
    @Config.Comment("Lowest height at which Worley's Caves should generate in a cubic world generator.")
    public static int minCaveY = -5000;
    @Config.Comment("Should we generate up to the top of the world, or only up to maxGenerationY?")
    public static boolean noMaximumY = false;
    @Config.Comment("Maximum height at which Worley's Caves should generate in a cubic world generator.")
    public static int maxCaveY = 64;

    @Config.Comment({
            "How deep lava should be, relative to minCaveY, in a cubic world generator.",
            "\tLava (or whichever block is specified in the Worley's Caves config) will fill",
            "\tspaces from minCaveY to minCaveY+lavaDepth, unless noMaximumY and/or noMinimumY",
            "\tis true.",
            "Please note that this works differently from the lavaDepth variable in the Worley's",
            "\tCaves config, as that sets the y-level lava will start generating at, while the",
            "\tlavaDepth parameter in this file controls the depth relative to minCaveY"
    })
    public static int lavaDepth = 100;

    @Config.Comment({
            "Should we use random Perlin noise to randomly distort the Worley caves in a",
            "\tcubic world generator? This is recommended if the maxY-minY is very large (or",
            "\tif maxY/minY are disabled), as it increases the variation in the caves."
    })
    public static boolean useWarpNoise = true;

    @Config.Comment({
            "Should caves become more warped the further down in the world you go? This",
            "\tonly applies if noMinimumY & noMaximumY are both false. Can be combined",
            "\twith warp noise."
    })
    public static boolean depthIncreasesCaveWarping = true;
}
