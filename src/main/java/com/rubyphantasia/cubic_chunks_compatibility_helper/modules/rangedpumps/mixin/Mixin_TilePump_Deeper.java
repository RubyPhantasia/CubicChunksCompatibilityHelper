package com.rubyphantasia.cubic_chunks_compatibility_helper.modules.rangedpumps.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.raoulvdberge.rangedpumps.tile.TilePump;
import com.rubyphantasia.cubic_chunks_compatibility_helper.ModLogger;
import com.rubyphantasia.cubic_chunks_compatibility_helper.config.ConfigRangedPumps;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.rangedpumps.constants.Constants_RangedPump_NBT;
import com.rubyphantasia.cubic_chunks_compatibility_helper.util.Miscellaneous;
import io.github.opencubicchunks.cubicchunks.api.world.IMinMaxHeight;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Queue;

@Mixin(TilePump.class)
public abstract class Mixin_TilePump_Deeper extends TileEntity {
    @Shadow @Nullable private BlockPos currentPos;
    @Shadow
    private Queue<BlockPos> surfaces;
    private int maxDepth = Integer.MAX_VALUE;
    private IMinMaxHeight worldMinMaxHeight = new IMinMaxHeight() {};

    @Redirect(method="update",
                at=@At(value="INVOKE", target="Lnet/minecraft/util/math/BlockPos;getY()I"))
    public int allowPumpingAcrossYZero(BlockPos pos) {
        return (pos.getY() <= maxDepth || pos.getY() <= worldMinMaxHeight.getMinHeight()) ? 0 : 1;
    }

    @Override
    public void setWorld(World worldIn) {
        super.setWorld(worldIn);
        worldMinMaxHeight = Miscellaneous.fetchOrCreateMinMaxHeight(worldIn);
    }

    public void setMaxDepth() {
        maxDepth = Math.max(ConfigRangedPumps.deepestPumpableY, pos.getY()-ConfigRangedPumps.maximumRelativeDepth);
    }

    @Inject(method="rebuildSurfaces",
            at=@At(value="INVOKE", target="Lnet/minecraft/util/math/BlockPos;down()Lnet/minecraft/util/math/BlockPos;"))
    public void setMaxDepthWhenInitialized(CallbackInfo ci) {
        setMaxDepth();
    }

    @Inject(method="readFromNBT",
            at=@At("TAIL"))
    public void setMaxDepthWhenLoading(CallbackInfo ci) {
        setMaxDepth();
    }

    @ModifyExpressionValue(method="getState",
                            at=@At(value = "FIELD", opcode=Opcodes.GETFIELD, target = "Lcom/raoulvdberge/rangedpumps/RangedPumps;range:I"), remap=false)
    public int ccch_setDoneIfBelowPumpingYLimit(int original) {
        if ((this.pos.getY()-1) < ConfigRangedPumps.deepestPumpableY) {
            return -2;
        } else {
            return original;
        }
    }


    // Correct the block pos computed, when computing it from a long value.
    @ModifyExpressionValue(method="readFromNBT",
            at=@At(value= "INVOKE", target="Lnet/minecraft/util/math/BlockPos;fromLong(J)Lnet/minecraft/util/math/BlockPos;"), expect=2, allow=2)
    public BlockPos guessActualBlockPos(BlockPos original) {
        return Miscellaneous.attemptToCorrectTruncatedYValue(original, this.pos.getY());
    }

    // Ensure we read the non-truncated current position, if it is available.
    @ModifyExpressionValue(method="readFromNBT",
            at=@At(value= "INVOKE", target="Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z", ordinal=1))
    public boolean shouldReadTruncatedPosition(boolean original, NBTTagCompound tag) {
        return original && !tag.hasKey(Constants_RangedPump_NBT.CCCH_CURRENT_POS_KEY);
    }

    @Inject(method="readFromNBT", at= @At("TAIL"))
    public void readNonTruncatedCurrentPosition(NBTTagCompound tag, CallbackInfo ci) {
        if (tag.hasKey("CurrentPos") && tag.hasKey(Constants_RangedPump_NBT.CCCH_CURRENT_POS_KEY)) {
            this.currentPos = Miscellaneous.NBTToBlockPos(tag.getCompoundTag(Constants_RangedPump_NBT.CCCH_CURRENT_POS_KEY));
        }
    }

    // Ensure we read the non-truncated surfaces, if they are available.
    @ModifyExpressionValue(method="readFromNBT", at= @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NBTTagCompound;hasKey(Ljava/lang/String;)Z", ordinal=3))
    public boolean checkForNonTruncatedSurfaces(boolean original, NBTTagCompound tag) {
        return original && !tag.hasKey(Constants_RangedPump_NBT.CCCH_SURFACES_KEY);
    }

    @Inject(method="readFromNBT", at= @At(value = "INVOKE", target = "Lnet/minecraftforge/fluids/FluidTank;readFromNBT(Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraftforge/fluids/FluidTank;"))
    public void readNonTruncatedSurfaces(NBTTagCompound tag, CallbackInfo ci) {
        if (tag.hasKey("CurrentPos") && tag.hasKey(Constants_RangedPump_NBT.CCCH_SURFACES_KEY)) {
            NBTTagList nbtSurfaces = tag.getTagList(Constants_RangedPump_NBT.CCCH_SURFACES_KEY, 10);
            nbtSurfaces.forEach(nbtBlockPos -> surfaces.add(Miscellaneous.NBTToBlockPos((NBTTagCompound) nbtBlockPos)));
        }
    }

    @Inject(method="writeToNBT", at=@At("TAIL"))
    public void writeFullBlockPositions(NBTTagCompound tag, CallbackInfoReturnable<NBTTagCompound> ci) {
        if (this.currentPos != null) {
            tag.setTag(Constants_RangedPump_NBT.CCCH_CURRENT_POS_KEY, Miscellaneous.blockPosToNBT(this.currentPos));
        }
        NBTTagList surfacesFullBlockPositions = new NBTTagList();
        surfaces.forEach(blockPos -> surfacesFullBlockPositions.appendTag(Miscellaneous.blockPosToNBT(blockPos)));
        tag.setTag(Constants_RangedPump_NBT.CCCH_SURFACES_KEY, surfacesFullBlockPositions);
    }
}
