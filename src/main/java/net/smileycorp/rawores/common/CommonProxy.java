package net.smileycorp.rawores.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {
    
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
    
    public void init(FMLInitializationEvent event) {}
    
    public void postInit(FMLPostInitializationEvent event) {
        //we do everything in post init so we can check everything other mods have registered
        OreHandler.INSTANCE.buildOreProperties();
    }
    
    public void serverStart(FMLServerStartingEvent event) {}
    
}
