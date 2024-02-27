package com.rubyphantasia.cubic_chunks_compatibility_helper.proxy;

import com.rubyphantasia.cubic_chunks_compatibility_helper.ModLogger;
import com.rubyphantasia.cubic_chunks_compatibility_helper.modules.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    ModuleManager moduleManager = new ModuleManager();
    public void registerItemRenderer(Item item, int meta, String id)
    {

    }

    public void preInit(FMLPreInitializationEvent event)
    {
        ModLogger.info("CommonProxy#preInit");
        // some example code
//        Mod_CubicChunksCompatibilityHelper.info(String.format("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName()));
        moduleManager.setupModules();
        moduleManager.runModulesPreInit();
    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void registerBlocks(RegistryEvent.Register<Block> event)
    {

    }

    public void registerItems(RegistryEvent.Register<Item> event)
    {

    }

    public void registerModels(ModelRegistryEvent event)
    {

    }
}
