package net.smileycorp.dynaores.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.smileycorp.dynaores.common.network.PacketHandler;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class DynaOres {
    
    public static final CreativeTabs CREATIVE_TAB = new DynaOresTab();

    @Mod.Instance(Constants.MODID)
    public static DynaOres INSTANCE;
    
    @SidedProxy(clientSide = Constants.CLIENT_PROXY, serverSide = Constants.SERVER_PROXY)
    public static CommonProxy PROXY;
    
    public DynaOres() {
        DynaOresLogger.clearLog();
        PacketHandler.init();
        if (Loader.isModLoaded("crafttweaker"))  MinecraftForge.EVENT_BUS.register(new CraftTweakerIntegration());
    }
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        PROXY.preInit(event);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        PROXY.postInit(event);
    }
    
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event){
        PROXY.serverStarting(event);
    }
    
}
