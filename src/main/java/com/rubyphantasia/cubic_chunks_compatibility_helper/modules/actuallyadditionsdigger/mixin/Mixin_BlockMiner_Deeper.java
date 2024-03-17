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
