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

import com.rubyphantasia.cubic_chunks_compatibility_helper.config.ConfigActuallyAdditionsDigger;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.actuallyadditionsdigger.interfaces.ITileMiner_Deeper;
import com.rubyphantasia.cubic_chunks_compatibility_helper.util.Miscellaneous;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityInventoryBase;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityMiner;
import io.github.opencubicchunks.cubicchunks.api.world.IMinMaxHeight;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.rubyphantasia.cubic_chunks_compatibility_helper.modules.actuallyadditionsdigger.constants.Constants_Miner_Deeper.*;

@Mixin(value=TileEntityMiner.class, remap=false)
public abstract class Mixin_TileMiner_Deeper extends TileEntityInventoryBase implements ITileMiner_Deeper {
    @Unique
    private int ccch_maxDepth = Integer.MAX_VALUE;
    @Unique
    private IMinMaxHeight ccch_worldMinMaxHeight = new IMinMaxHeight() {};

    @Shadow
    public int checkY;

    public Mixin_TileMiner_Deeper(int slots, String name) {
        super(slots, name);
    }

    @Unique
    private void ccch_setMaxDepth() {
        ccch_maxDepth = Math.max(ConfigActuallyAdditionsDigger.deepestDiggableY, pos.getY()-ConfigActuallyAdditionsDigger.maximumRelativeDepth);
    }

    public boolean ccch_isDoneMining() {
        boolean inProgressOrFinished = checkY != NOT_STARTED_Y_VALUE;
        boolean pastDepthLimit = checkY< ccch_maxDepth || checkY < ccch_worldMinMaxHeight.getMinHeight();
        return inProgressOrFinished && pastDepthLimit;
    }

    @Inject(method="<init>()V", at=@At("RETURN"))
    public void ccch_initializeCheckY(CallbackInfo ci) {
        checkY = NOT_STARTED_Y_VALUE;
    }

    @Redirect(method="updateEntity",
            at=@At(value="FIELD", opcode=Opcodes.GETFIELD, target="Lde/ellpeck/actuallyadditions/mod/tile/TileEntityMiner;checkY:I", ordinal=0),
            require=1, allow=1)
    public int ccch_fixYEqualsZero(TileEntityMiner miner) {
        return ccch_isDoneMining() ? 0 : 1;
    }

    @ModifyConstant(method="updateEntity",
            constant={@Constant(expandZeroConditions={Constant.Condition.LESS_THAN_ZERO})},
            require=1, allow=1)
    public int ccch_resetDetection(int oldConstant) {
        return NOT_STARTED_Y_VALUE+1;
    }

    @Inject(method="updateEntity",
            at=@At(value="FIELD", opcode=Opcodes.GETFIELD, target="Lde/ellpeck/actuallyadditions/mod/tile/TileEntityMiner;checkY:I", ordinal=2),
            require=1, allow=1)
    public void ccch_onReset(CallbackInfo ci) {
        ccch_setMaxDepth();
    }

    @ModifyConstant(method="updateEntity",
            constant={@Constant(expandZeroConditions={Constant.Condition.GREATER_THAN_ZERO})},
            require=1, allow=1)
    public int ccch_mineBelowZero(int oldConstant) {
        // Minus one b/c it mines if y>returnValue, and we want it to mine at the maxdepth/lowest block.
        return Math.max(ccch_maxDepth, ccch_worldMinMaxHeight.getMinHeight())-1;
    }

    @Override
    public void setWorld(World worldIn) {
        super.setWorld(worldIn);
        ccch_worldMinMaxHeight = Miscellaneous.fetchOrCreateMinMaxHeight(worldIn);
    }

    @ModifyConstant(method="onButtonPressed",
                    constant=@Constant(intValue=-1),
                    require=1, allow = 1)
    public int setCheckYToNotStartedValue(int oldValue) {
        return NOT_STARTED_Y_VALUE;
    }

    @Inject(method="readSyncableNBT", at=@At("TAIL"))
    public void createdFromNBT(CallbackInfo ci) {
        ccch_setMaxDepth();
    }
}
