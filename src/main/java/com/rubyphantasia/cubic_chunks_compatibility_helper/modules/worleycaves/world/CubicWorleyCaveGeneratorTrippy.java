package com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves.world;

import com.google.common.base.MoreObjects;
import fluke.worleycaves.config.Configs;
import fluke.worleycaves.util.FastNoise;
import fluke.worleycaves.util.WorleyUtil;
import io.github.opencubicchunks.cubicchunks.api.util.Coords;
import io.github.opencubicchunks.cubicchunks.api.util.CubePos;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.api.worldgen.structure.ICubicStructureGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This code uses the MIT license, and is heavily derived from the original 1.12.2 Worley's Caves mod
 *  (<a href=http://github.com/superfluke/WorleyCaves/tree/1.12.2>http://github.com/superfluke/WorleyCaves/tree/1.12.2</a>) (which is also MIT licensed). <br/>
 *  <br/>
 *
 * The MIT License (MIT) <br/>
 *
 * Copyright (c) 2018 <br/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: <br/>
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. <br/>
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. <br/>
 */

public class CubicWorleyCaveGeneratorTrippy implements ICubicStructureGenerator {
    private static final int xSubchunkSize = 4;
    private static final int ySubchunkSize = 2;
    private static final int zSubchunkSize = 4;
    private static final int xSubChunks = ICube.SIZE/xSubchunkSize;
    private static final int ySubChunks = ICube.SIZE/ySubchunkSize;
    private static final int zSubChunks = ICube.SIZE/zSubchunkSize;

    private WorleyUtil worleyF1divF3 = new WorleyUtil();
    private FastNoise displacementNoisePerlinX = new FastNoise();
    private FastNoise displacementNoisePerlinY = new FastNoise();
    private FastNoise displacementNoisePerlinZ = new FastNoise();
    private World world;

    private static final IBlockState AIR;
    private static final IBlockState BLK_SANDSTONE;
    private static final IBlockState BLK_RED_SANDSTONE;
    private static final Set<Block> SIMPLE_REPLACE_BLOCKS;
    private static float noiseCutoff;
    private static float yCompression;
    private static float xzCompression;
    private static boolean additionalWaterChecks;

    public CubicWorleyCaveGeneratorTrippy() {
        this.worleyF1divF3.SetFrequency(0.016F);
        displacementNoisePerlinX.SetNoiseType(FastNoise.NoiseType.Perlin);
        displacementNoisePerlinX.SetFrequency(0.05F);
        displacementNoisePerlinY.SetNoiseType(FastNoise.NoiseType.Perlin);
        displacementNoisePerlinY.SetFrequency(0.05F);
        displacementNoisePerlinZ.SetNoiseType(FastNoise.NoiseType.Perlin);
        displacementNoisePerlinZ.SetFrequency(0.05F);
        noiseCutoff = (float)Configs.cavegen.noiseCutoffValue;
        xzCompression = (float)Configs.cavegen.horizonalCompressionMultiplier;
        yCompression = (float)Configs.cavegen.verticalCompressionMultiplier;
        additionalWaterChecks = Loader.isModLoaded("subterraneenwaters"); // Original Worley's Caves spelled this "subterranaenwaters"

    }

    @Override
    public void generate(World world, CubePrimer cube, CubePos cubePos) {
        float[][][] samples = this.sampleNoise(cubePos);
        this.world = world;
        final int seaLevel = world.getSeaLevel();
        final float oneHalf = 0.5F;
        final float oneQuarter = 0.25F;
        for (int subChunkX = 0; subChunkX < xSubChunks; subChunkX++) {
            for (int subchunkZ = 0; subchunkZ < zSubChunks; subchunkZ++) {
                for (int subchunkY = ySubChunks-1; subchunkY >= 0; subchunkY--) {
                    float x0y0z0 = samples[subChunkX][subchunkY][subchunkZ];
                    float x0y0z1 = samples[subChunkX][subchunkY][subchunkZ + 1];
                    float x1y0z0 = samples[subChunkX + 1][subchunkY][subchunkZ];
                    float x1y0z1 = samples[subChunkX + 1][subchunkY][subchunkZ + 1];
                    float x0y1z0 = samples[subChunkX][subchunkY + 1][subchunkZ];
                    float x0y1z1 = samples[subChunkX][subchunkY + 1][subchunkZ + 1];
                    float x1y1z0 = samples[subChunkX + 1][subchunkY + 1][subchunkZ];
                    float x1y1z1 = samples[subChunkX + 1][subchunkY + 1][subchunkZ + 1];
                    float noiseStepY00 = (x0y1z0 - x0y0z0) * -oneHalf;
                    float noiseStepY01 = (x0y1z1 - x0y0z1) * -oneHalf;
                    float noiseStepY10 = (x1y1z0 - x1y0z0) * -oneHalf;
                    float noiseStepY11 = (x1y1z1 - x1y0z1) * -oneHalf;
                    float noiseStartX0 = x0y0z0;
                    float noiseStartX1 = x0y0z1;
                    float noiseEndX0 = x1y0z0;
                    float noiseEndX1 = x1y0z1;
                    for (int subchunkLocalY = ySubchunkSize-1; subchunkLocalY >= 0; subchunkLocalY--) {
                        int localY = subchunkLocalY + subchunkY*ySubchunkSize;
                        float noiseStartZ = noiseStartX0;
                        float noiseEndZ = noiseStartX1;
                        float noiseStepX0 = (noiseEndX0 - noiseStartX0) * oneQuarter;
                        float noiseStepX1 = (noiseEndX1 - noiseStartX1) * oneQuarter;

                        for (int subchunkLocalX = 0; subchunkLocalX < xSubchunkSize; subchunkLocalX++) {
                            int localX = subchunkLocalX + subChunkX*xSubchunkSize;
                            int realX = Coords.localToBlock(cubePos.getX(), localX);
                            float noiseStepZ = (noiseEndZ - noiseStartZ) * oneQuarter;
                            float noiseVal = noiseStartZ;

                            for (int subchunkLocalZ = 0; subchunkLocalZ < zSubchunkSize; subchunkLocalZ++) {
                                int localZ = subchunkLocalZ + subchunkZ*zSubchunkSize;
                                int realZ = Coords.localToBlock(cubePos.getZ(), localZ);
                                // TODO Stuff regarding depth

                                float adjustedNoiseCutoff = noiseCutoff;

                                // TODO stuff w/ localY < minCaveHeight+5 (maybe)

                                if (noiseVal > adjustedNoiseCutoff) {
                                    // FIXME Can't check the block above for blocks at the top of this cube, as that would be the start of the next cube.
                                    IBlockState aboveBlock = (IBlockState) MoreObjects.firstNonNull(cube.getBlockState(localX, Math.min(localY+1, ICube.SIZE-1), localZ), Blocks.AIR.getDefaultState());
                                    if (aboveBlock.getMaterial() != Material.WATER) {
                                        // FIXME Maybe something with lavaDepth?
                                        if ((localY > seaLevel - 8 || additionalWaterChecks)
                                                && (localX < ICube.SIZE && cube.getBlockState(localX + 1, localY, localZ).getMaterial() == Material.WATER
                                                    || localX > 0 && cube.getBlockState(localX - 1, localY, localZ).getMaterial() == Material.WATER
                                                    || localZ < ICube.SIZE && cube.getBlockState(localX, localY, localZ + 1).getMaterial() == Material.WATER
                                                    || localZ > 0 && cube.getBlockState(localX, localY, localZ - 1).getMaterial() == Material.WATER)) {
                                            continue;
                                        }

                                        IBlockState currentBlock = cube.getBlockState(localX, localY, localZ);
                                        boolean foundTopBlock = false;
                                        if (this.isTopBlock(cube, localX, localY, localZ, cubePos)) {
                                            foundTopBlock = true;
                                        }

                                        this.digBlock(cube, localX, localY, localZ, cubePos, foundTopBlock, currentBlock, aboveBlock);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    Biome getBiome(CubePrimer cube, int blockX, int blockY, int blockZ) {
        Biome biome = cube.getBiome(Coords.blockToLocalBiome3d(blockX), Coords.blockToLocalBiome3d(blockY), Coords.blockToLocalBiome3d(blockZ));
        if (biome == null) {
            biome = this.world.getBiome(new BlockPos(blockX, blockY, blockZ));
        }
        return biome;
    }

    private boolean isTopBlock(CubePrimer cube, int localX, int localY, int localZ, CubePos cubePos) {

        Biome biome = getBiome(cube, Coords.localToBlock(cubePos.getX(), localX), Coords.localToBlock(cubePos.getY(), localY), Coords.localToBlock(cubePos.getZ(), localZ));
        IBlockState state = cube.getBlockState(localX, localY, localZ);
        return this.isExceptionBiome(biome) ? state.getBlock() == Blocks.GRASS : state == biome.topBlock;
    }

    private boolean isExceptionBiome(Biome biome) {
        if (biome == Biomes.BEACH) {
            return true;
        } else {
            return biome == Biomes.DESERT;
        }
    }

    private boolean canReplaceBlock(IBlockState state, IBlockState up) {
        if (SIMPLE_REPLACE_BLOCKS.contains(state.getBlock())) {
            return true;
        } else {
            return (state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.GRAVEL) && up.getMaterial() != Material.WATER;
        }
    }

    protected void digBlock(CubePrimer cube, int localX, int localY, int localZ, CubePos cubePos, boolean foundTop, IBlockState state, IBlockState up) {
        Biome biome = getBiome(cube, Coords.localToBlock(cubePos.getX(), localX), Coords.localToBlock(cubePos.getY(), localY), Coords.localToBlock(cubePos.getZ(), localZ));
        IBlockState top = biome.topBlock;
        IBlockState filler = biome.fillerBlock;
        if (this.canReplaceBlock(state, up) || state.getBlock() == top.getBlock() || state.getBlock() == filler.getBlock()) {
            // FIXME Maybe stuff with y <= lavaDepth
            cube.setBlockState(localX, localY, localZ, AIR);
            if (localY > 0) {
                if (foundTop && cube.getBlockState(localX, localY - 1, localZ).getBlock() == filler.getBlock()) {
                    cube.setBlockState(localX, localY - 1, localZ, top);
                }
            }
            if ((localY+1) < ICube.SIZE) {
                if (up == Blocks.SAND.getDefaultState()) {
                    cube.setBlockState(localX, localY + 1, localZ, BLK_SANDSTONE);
                } else if (up == Blocks.SAND.getStateFromMeta(1)) {
                    cube.setBlockState(localX, localY + 1, localZ, BLK_RED_SANDSTONE);
                }
            }
        }

    }

    public float[][][] sampleNoise(CubePos cubePos) {
        final int nPointsBelow = 2;
        final int xSamplePoints = xSubChunks+1;
        final int ySamplePoints = ySubChunks+1;
        final int zSamplePoints = zSubChunks+1;
        float [][][] noiseSamples = new float[xSamplePoints][ySamplePoints][zSamplePoints];
        for (int xSampleIndex = 0; xSampleIndex < xSamplePoints; xSampleIndex++) {
            final int localX = xSampleIndex*xSubChunks;
            int x = Coords.localToBlock(cubePos.getX(), localX);
            for (int zSampleIndex = 0; zSampleIndex < zSamplePoints; zSampleIndex++) {
                final int localZ = zSampleIndex*zSubChunks;
                int z = Coords.localToBlock(cubePos.getZ(), localZ);
                for (int ySampleIndex = ySubChunks; ySampleIndex >= -nPointsBelow; ySampleIndex--) {
                    final int localY = ySampleIndex*ySubChunks;
                    float y = Coords.localToBlock(cubePos.getY(), localY);
                    // TODO Handle "approximate surface height" the same way WorleyCaves does.
                    // TODO handle min/max cave height?

                    // TODO Handle cave warping
//                    float dispAmp = (float)((double)warpAmplifier * ((double)(originalMaxHeight - y) / ((double)originalMaxHeight * 0.85D)));
                    float dispAmp = 1.0F;
                    float xDisp = 0.0F;
                    float yDisp = 0.0F;
                    float zDisp = 0.0F;
                    xDisp = this.displacementNoisePerlinX.GetNoise((float)x, y, (float)z) * dispAmp;
                    yDisp = this.displacementNoisePerlinY.GetNoise((float)x, y, (float)z) * dispAmp;
                    zDisp = this.displacementNoisePerlinZ.GetNoise((float)x, y, (float)z) * dispAmp;
                    float noise = this.worleyF1divF3.SingleCellular3Edge((float)x * xzCompression + xDisp, y * yCompression + yDisp, (float)z * xzCompression + zDisp);
                    if (ySampleIndex >= 0) {
                        noiseSamples[xSampleIndex][ySampleIndex][zSampleIndex] = noise;
                    }
                    if (noise > noiseCutoff) {
                        if (ySampleIndex >= 0) {
                            if (xSampleIndex > 0) {
                                noiseSamples[xSampleIndex - 1][ySampleIndex][zSampleIndex] = noise * 0.2F + noiseSamples[xSampleIndex - 1][ySampleIndex][zSampleIndex] * 0.8F;
                            }

                            if (zSampleIndex > 0) {
                                noiseSamples[xSampleIndex][ySampleIndex][zSampleIndex - 1] = noise * 0.2F + noiseSamples[xSampleIndex][ySampleIndex][zSampleIndex - 1] * 0.8F;
                            }
                        }

                        int ySampleIndexOneAbove = ySampleIndex+1;
                        if (ySampleIndexOneAbove >= 0 && ySampleIndexOneAbove < ySamplePoints) {
                            float noiseAbove = noiseSamples[xSampleIndex][ySampleIndexOneAbove][zSampleIndex];
                            if (noise > noiseAbove) {
                                noiseSamples[xSampleIndex][ySampleIndexOneAbove][zSampleIndex] = noise * 0.8F + noiseAbove * 0.2F;
                            }
                        }

                        int ySampleIndexTwoAbove = ySampleIndex+2;
                        if (ySampleIndexTwoAbove < ySamplePoints) {
                            float noiseTwoAbove = noiseSamples[xSampleIndex][ySampleIndexTwoAbove][zSampleIndex];
                            if (noise > noiseTwoAbove) {
                                noiseSamples[xSampleIndex][ySampleIndexTwoAbove][zSampleIndex] = noise * 0.35F + noiseTwoAbove * 0.65F;
                            }
                        }
                    }
                }
            }
        }

        return noiseSamples;
    }

    static {
        AIR = Blocks.AIR.getDefaultState();
        BLK_SANDSTONE = Blocks.SANDSTONE.getDefaultState();
        BLK_RED_SANDSTONE = Blocks.RED_SANDSTONE.getDefaultState();
        Set<Block> replaceBlocks = new HashSet<>();
        replaceBlocks.add(Blocks.STONE);
        replaceBlocks.add(Blocks.DIRT);
        replaceBlocks.add(Blocks.GRASS);
        replaceBlocks.add(Blocks.HARDENED_CLAY);
        replaceBlocks.add(Blocks.STAINED_HARDENED_CLAY);
        replaceBlocks.add(Blocks.SANDSTONE);
        replaceBlocks.add(Blocks.RED_SANDSTONE);
        replaceBlocks.add(Blocks.MYCELIUM);
        replaceBlocks.add(Blocks.SNOW_LAYER);
        SIMPLE_REPLACE_BLOCKS = Collections.unmodifiableSet(replaceBlocks);

    }
}
