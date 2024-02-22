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
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Mod_CubicChunksCompatibilityHelper.MODID, name = Mod_CubicChunksCompatibilityHelper.NAME, version = Mod_CubicChunksCompatibilityHelper.VERSION,
dependencies = "required-after:cubicchunks@[0.0.1271.0,);"
                + "required-after:mixinbooter@[9.1,);"
                + "after:actuallyadditions@[r148,);"
                + "after:rangedpumps@[0.5,);"
                + "after:worleycaves@[1.4.1,);")
public class Mod_CubicChunksCompatibilityHelper
{
    public static final String MODID = "cubic_chunks_compatibility_helper";
    public static final String NAME = "Cubic Chunks Compatibility Helper";
    public static final String VERSION = "0.0.1";
    public static final String packageName = "com.rubyphantasia.cubic_chunks_compatibility_helper";

    private static Logger logger = LogManager.getLogger(NAME);

    @Mod.Instance
    public static Mod_CubicChunksCompatibilityHelper instance;

    @SidedProxy (clientSide=packageName+".proxy.ClientProxy", serverSide=packageName+".proxy.CommonProxy")
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

    public static void info(String message) {
        logger.info(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void fatal(String message) {
        logger.fatal(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }
}
