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
@Config(modid=ModInfo.MODID, name=ModInfo.MODULE_CONFIGS_PATH_PREFIX +"rangedpumps")
public class ConfigRangedPumps {

    @SuppressWarnings("unused")
    @Config.Comment({
            "The deepest a pump can pump down to is its y-level minus maximumRelativeDepth, or",
            "\tdeepestPumpableY, whichever is higher in the world.",
            "This variable does nothing."
    })
    public static boolean _explanation = true;

    @Config.Comment("How far a pump can pump below itself.")
    public static int maximumRelativeDepth = 300;

    @Config.Comment("Deepest y-level a pump can pump at.")
    public static int deepestPumpableY = -5000;
}
