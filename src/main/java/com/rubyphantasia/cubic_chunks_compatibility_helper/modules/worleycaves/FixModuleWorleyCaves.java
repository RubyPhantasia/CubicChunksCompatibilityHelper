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

package com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModLogger;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.IFixModule;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves.event.CubicCaveEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class FixModuleWorleyCaves implements IFixModule {
    public FixModuleWorleyCaves() {}

    @Override
    public void setupModule() {
        ModLogger.info("Hello from WorleyCavesFixModule#setupModule.");
    }

    @Override
    public void preInit() {
        MinecraftForge.TERRAIN_GEN_BUS.register(new CubicCaveEventHandler());
    }
}
