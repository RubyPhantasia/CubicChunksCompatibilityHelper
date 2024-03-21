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

@Config.RequiresMcRestart
@Config(modid=ModInfo.MODID)
public class ConfigGeneral {
    @Config.Comment({
            "Should we perform an in-depth scan of most loaded classes for common Cubic Chunks incompatibilities?"
    })
    public static boolean scanClassesForIncompatibilities = false;
}
