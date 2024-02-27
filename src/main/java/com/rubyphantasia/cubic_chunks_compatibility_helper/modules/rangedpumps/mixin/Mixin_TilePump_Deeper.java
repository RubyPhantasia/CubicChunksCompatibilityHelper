package com.rubyphantasia.cubic_chunks_compatibility_helper.modules.rangedpumps.mixin;

import com.raoulvdberge.rangedpumps.tile.TilePump;
import com.rubyphantasia.cubic_chunks_compatibility_helper.util.Miscellaneous;
import io.github.opencubicchunks.cubicchunks.api.world.IMinMaxHeight;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TilePump.class)
public abstract class Mixin_TilePump_Deeper extends TileEntity {
    private final int MAX_RELATIVE_DEPTH = 300;
    private final int MAX_ABSOLUTE_DEPTH = -10000;
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
        maxDepth = Math.max(MAX_ABSOLUTE_DEPTH, pos.getY()-MAX_RELATIVE_DEPTH);
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
}
