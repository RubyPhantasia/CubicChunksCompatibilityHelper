package com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves.event;

import com.rubyphantasia.cubic_chunks_compatibility_helper.Mod_CubicChunksCompatibilityHelper;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.worleycaves.world.CubicWorleyCaveGenerator;
import fluke.worleycaves.config.Configs;
import io.github.opencubicchunks.cubicchunks.api.worldgen.structure.event.InitCubicStructureGeneratorEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CubicCaveEventHandler {
    public CubicCaveEventHandler() {}

    @SubscribeEvent
    public void structureEvent(InitCubicStructureGeneratorEvent event) {
        Mod_CubicChunksCompatibilityHelper.info("Received event: "+event);
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
            Mod_CubicChunksCompatibilityHelper.info("Registering CubicWorleyCaveGenerator2.");
            event.setNewGen(new CubicWorleyCaveGenerator(event.getWorld()));
        }
    }
}
