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

package com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves.event;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModLogger;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves.world.CubicWorleyCaveGenerator;
import fluke.worleycaves.config.Configs;
import io.github.opencubicchunks.cubicchunks.api.worldgen.structure.event.InitCubicStructureGeneratorEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CubicCaveEventHandler {
    public CubicCaveEventHandler() {}

    @SubscribeEvent
    public void structureEvent(InitCubicStructureGeneratorEvent event) {
        ModLogger.info("Received event: "+event);
        if (event.getType() == InitMapGenEvent.EventType.CAVE) {
            // FIXME it seems a provider's dimensionID can be set at any time - could that be a problem?
            //  If it is, I might have to do things the way Worley's Caves does it, where I check the dimensionID
            //  on each call to CubicWorleyCaveGenerator#generate
            int worldDimension = event.getWorld().provider.getDimension();
            int[] blackListedDims = Configs.cavegen.blackListedDims;
            int nBlacklistedDims = blackListedDims.length;
            for (int i = 0; i < nBlacklistedDims; ++i) {
                int blacklistedDim = blackListedDims[i];
                if (worldDimension == blacklistedDim) {
                    return;
                }
            }
            ModLogger.info("Registering CubicWorleyCaveGenerator2.");
            event.setNewGen(new CubicWorleyCaveGenerator(event.getWorld()));
        }
    }
}
