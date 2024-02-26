package com.rubyphantasia.cubic_chunks_compatibility_helper;

import com.rubyphantasia.cubic_chunks_compatibility_helper.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION,
dependencies = "required-after:cubicchunks@[0.0.1271.0,);"
                + "required-after:mixinbooter@[9.1,);"
                + "after:actuallyadditions@[r148,);"
                + "after:rangedpumps@[0.5,);"
                + "after:worleycaves@[1.4.1,);")
public class Mod_CubicChunksCompatibilityHelper
{
    @Mod.Instance
    public static Mod_CubicChunksCompatibilityHelper instance;

    @SidedProxy (clientSide= ModInfo.packageName+".proxy.ClientProxy", serverSide= ModInfo.packageName+".proxy.CommonProxy")
    public static CommonProxy proxy;

    public Mod_CubicChunksCompatibilityHelper()
    {
        MinecraftForge.EVENT_BUS.register(this);
//        logger = LogManager.getLogger(NAME);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void postLoad(FMLLoadCompleteEvent event) {
        proxy.postLoad(event);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event)
    {
        proxy.registerBlocks(event);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event)
    {
        proxy.registerItems(event);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event)
    {
        proxy.registerModels(event);
    }
}
