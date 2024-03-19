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

package com.rubyphantasia.cubic_chunks_compatibility_helper.modules.actuallyadditionsdigger.mixin;

import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.actuallyadditionsdigger.constants.Constants_Miner_Deeper;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.actuallyadditionsdigger.interfaces.ITileMiner_Deeper;
import de.ellpeck.actuallyadditions.mod.blocks.BlockMiner;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityMiner;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value=BlockMiner.class, remap=false)
public class Mixin_BlockMiner_Deeper {
    @Redirect(method="displayHud",
            at=@At(value="FIELD", opcode= Opcodes.GETFIELD, target="Lde/ellpeck/actuallyadditions/mod/tile/TileEntityMiner;checkY:I", ordinal=0))
    public int ccch_fixDoneMining(TileEntityMiner miner) {
        return (((ITileMiner_Deeper)((Object)miner)).ccch_isDoneMining()) ? 0 : 1;
    }

    @ModifyConstant(method="displayHud",
                    constant=@Constant(intValue=-1),
                    require=1)
    public int ccch_fixCalculatingPositions(int oldConstant) {
        return Constants_Miner_Deeper.NOT_STARTED_Y_VALUE;
    }

}
