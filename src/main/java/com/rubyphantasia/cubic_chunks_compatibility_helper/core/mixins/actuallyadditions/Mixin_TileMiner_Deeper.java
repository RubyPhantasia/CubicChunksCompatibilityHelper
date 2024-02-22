package com.rubyphantasia.cubic_chunks_compatibility_helper.core.mixins.actuallyadditions;

import com.rubyphantasia.cubic_chunks_compatibility_helper.Mod_CubicChunksCompatibilityHelper;
import com.rubyphantasia.cubic_chunks_compatibility_helper.core.interfaces.actuallyadditions.ITileMiner_Deeper;
import com.rubyphantasia.cubic_chunks_compatibility_helper.util.Miscellaneous;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityInventoryBase;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityMiner;
import io.github.opencubicchunks.cubicchunks.api.world.IMinMaxHeight;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.rubyphantasia.cubic_chunks_compatibility_helper.core.constants.actuallyadditions.Constants_Miner_Deeper.*;

@Mixin(value=TileEntityMiner.class, remap=false)
public abstract class Mixin_TileMiner_Deeper extends TileEntityInventoryBase implements ITileMiner_Deeper {
    private int maxDepth = Integer.MAX_VALUE;
    private IMinMaxHeight worldMinMaxHeight = new IMinMaxHeight() {};

    @Shadow
    int checkY;

    public Mixin_TileMiner_Deeper(int slots, String name) {
        super(slots, name);
    }

    private void setMaxDepth() {
        maxDepth = Math.max(MAX_ABSOLUTE_DEPTH, pos.getY()-MAX_RELATIVE_DEPTH);
    }

    public boolean isDoneMining() {
        boolean inProgressOrFinished = checkY != NOT_STARTED_Y_VALUE;
        boolean pastDepthLimit = checkY<maxDepth || checkY < worldMinMaxHeight.getMinHeight();
        return inProgressOrFinished && pastDepthLimit;
    }

    @Inject(method="<init>()V", at=@At("RETURN"))
    public void X(CallbackInfo ci) {
        checkY = NOT_STARTED_Y_VALUE;
    }

    @Redirect(method="updateEntity",
            at=@At(value="FIELD", opcode=Opcodes.GETFIELD, target="Lde/ellpeck/actuallyadditions/mod/tile/TileEntityMiner;checkY:I", ordinal=0),
            require=1, allow=1)
    public int fixYEqualsZero(TileEntityMiner miner) {
        return isDoneMining() ? 0 : 1;
    }

    @ModifyConstant(method="updateEntity",
            constant={@Constant(expandZeroConditions={Constant.Condition.LESS_THAN_ZERO})},
            require=1, allow=1)
    public int resetDetection(int oldConstant) {
        return NOT_STARTED_Y_VALUE+1;
    }

    @Inject(method="updateEntity",
            at=@At(value="FIELD", opcode=Opcodes.GETFIELD, target="Lde/ellpeck/actuallyadditions/mod/tile/TileEntityMiner;checkY:I", ordinal=2),
            require=1, allow=1)
    public void onReset(CallbackInfo ci) {
        setMaxDepth();
    }

    @ModifyConstant(method="updateEntity",
            constant={@Constant(expandZeroConditions={Constant.Condition.GREATER_THAN_ZERO})},
            require=1, allow=1)
    public int mineBelowZero(int oldConstant) {
//        Mod_CubicChunksCompatibilityHelper.info("Y="+checkY);
        // Minus one b/c it mines if y>returnValue, and we want it to mine at the maxdepth/lowest block.
        return Math.max(maxDepth, worldMinMaxHeight.getMinHeight())-1;
    }

    @Override
    public void setWorld(World worldIn) {
        super.setWorld(worldIn);
        worldMinMaxHeight = Miscellaneous.fetchOrCreateMinMaxHeight(worldIn);
//        if (worldIn instanceof IMinMaxHeight) {
//            Mod_CubicChunksCompatibilityHelper.info("Cubic world detected.");
//            worldMinMaxHeight = (IMinMaxHeight) worldIn;
//        } else {
//            worldMinMaxHeight = new IMinMaxHeight() {
//                @Override
//                public int getMaxHeight() {
//                    return worldIn.getHeight();
//                }
//            };
//        }
    }

    @ModifyConstant(method="onButtonPressed",
                    constant=@Constant(intValue=-1),
                    require=1, allow = 1)
    public int setCheckYToNotStartedValue(int oldValue) {
        return NOT_STARTED_Y_VALUE;
    }

    @Inject(method="readSyncableNBT", at=@At("TAIL"))
    public void createdFromNBT(CallbackInfo ci) {
        setMaxDepth();
    }
}
