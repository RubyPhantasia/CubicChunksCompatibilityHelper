package com.rubyphantasia.cubic_chunks_compatibility_helper.util;

import io.github.opencubicchunks.cubicchunks.api.world.IMinMaxHeight;
import net.minecraft.world.World;

public class Miscellaneous {
    public static IMinMaxHeight fetchOrCreateMinMaxHeight(World world) {
        if (world instanceof IMinMaxHeight) {
            return (IMinMaxHeight) world;
        } else {
            return new IMinMaxHeight() {
                @Override
                public int getMaxHeight() {
                    return world.getHeight();
                }
            };
        }
    }
}
