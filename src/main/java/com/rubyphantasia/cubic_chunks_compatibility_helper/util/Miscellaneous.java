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

package com.rubyphantasia.cubic_chunks_compatibility_helper.util;

import com.rubyphantasia.cubic_chunks_compatibility_helper.core.mixin.Mixin_BlockPos_Accessor;
import io.github.opencubicchunks.cubicchunks.api.world.IMinMaxHeight;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class Miscellaneous {
    public static final String NBT_BLOCK_POS_X_KEY = "x";
    public static final String NBT_BLOCK_POS_Y_KEY = "y";
    public static final String NBT_BLOCK_POS_Z_KEY = "z";
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

    public static NBTTagCompound blockPosToNBT(BlockPos pos) {
        NBTTagCompound nbtPos = new NBTTagCompound();
        nbtPos.setInteger(NBT_BLOCK_POS_X_KEY, pos.getX());
        nbtPos.setInteger(NBT_BLOCK_POS_Y_KEY, pos.getY());
        nbtPos.setInteger(NBT_BLOCK_POS_Z_KEY, pos.getZ());
        return nbtPos;
    }

    public static BlockPos NBTToBlockPos(NBTTagCompound nbt) {
        return new BlockPos(
                nbt.getInteger(NBT_BLOCK_POS_X_KEY),
                nbt.getInteger(NBT_BLOCK_POS_Y_KEY),
                nbt.getInteger(NBT_BLOCK_POS_Z_KEY)
        );
    }

    /**
     * If a BlockPos with a y-value less than -2048 or greater than 2047 is converted to a long via BlockPos#toLong, that
     *  y-value loses all bits other than the lowest 12 bits, and the upper bit is treated as a sign bit. So a y-value
     *  of -2100 would become 1995, -2200 would become 1895, -4196 would become -101, etc. Effectively, all y-values are
     *  mapped to a single range, [-2048, 2048).
     *
     * This method guesses what the original y-value should be, given a known maximum value for it. It does so by
     *  first shifting the truncated y-value down into the maxYValue's range, by ORing with BlockPos.Y_MASK to extract the
     *  lower 12 bits of the truncated y-value (effectively shifting the negative part of the original range up above
     *  the positive part, to yield a wholly positive range, [0, 4096)), then ANDing it with the upper 20 bits of the
     *  specified maxYValue. Then, if the resultant value is greater than the maxYValue, it subtracts 4096 from it.
     *
     *  Effectively, this assumes that the original y-value is at most 4096 less than the specified maxYValue, and maps
     *  it accordingly. In the case where the original y-value and maxYValue are in the same "band" - they have the same
     *  upper 20-bits - this is just a matter of ANDing the original y-value with the specified maxYValue's upper 20 bits.
     *  In the case where the original y-value is in the upper end of the next-lower band, the previous step will map to
     *  the upper end of maxYValue's band, and be greater than maxYValue. In this case, you then need to also subtract
     *  4096 from the previous step's result, effectively shifting the previous step's result into the immediately lower
     *  band.
     *
     * @param origBlockPos Original block pos with 12-bit y-value
     * @param maxYValue
     * @return
     */
    public static BlockPos attemptToCorrectTruncatedYValue(BlockPos origBlockPos, int maxYValue) {
        int maxYValueUpperBits = maxYValue & ~(int)Mixin_BlockPos_Accessor.getY_MASK();
        int approximateMappedYValue = (origBlockPos.getY() & (int)Mixin_BlockPos_Accessor.getY_MASK()) | maxYValueUpperBits;
        if (approximateMappedYValue > maxYValue) {
            approximateMappedYValue -= (int)(Mixin_BlockPos_Accessor.getY_MASK()+1);
        }
        return new BlockPos(origBlockPos.getX(), approximateMappedYValue, origBlockPos.getZ());
    }

    public static boolean listContainsString(List<String> list, String str) {
        for (String arrStr : list) {
            if (str.equals(arrStr)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static String findPrefixInArray(String[] arr, String str) {
        for (String arrStr : arr) {
            if (str.startsWith(arrStr)) {
                return arrStr;
            }
        }
        return null;
    }

    /**
     * Calls operation on each stringFragment that is in containingString
     * @param stringFragments  Array of string fragments
     * @param containingString String that may contain string fragments
     * @param operation        Operation to execute for each fragment found int containingString
     */
    public static void forEachContainedFragmentInArray(String[] stringFragments, String containingString, Consumer<String> operation) {
        for (String stringFragment : stringFragments) {
            if (containingString.contains(stringFragment)) {
                operation.accept(stringFragment);
            }
        }
    }

    public static String convertClassNameToSlashFormat(String classNameRaw) {
        return classNameRaw.replace(".", "/");
    }
}
