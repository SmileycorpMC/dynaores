package net.smileycorp.dynaores.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, dependencies = Constants.DEPENDENCIES)
public class DynaOres {
    
    private static Logger logger;
    
    public static final CreativeTabs CREATIVE_TAB = new DynaOresTab();

    @Mod.Instance(Constants.MODID)
    public static DynaOres INSTANCE;
    
    @SidedProxy(clientSide = Constants.CLIENT_PROXY, serverSide = Constants.SERVER_PROXY)
    public static CommonProxy PROXY;
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        PROXY.preInit(event);
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        PROXY.postInit(event);
    }
    
    public static void logInfo(Object message) {
        logger.info(message);
    }
    
    public static void logError(Object message, Exception e) {
        logger.error(String.valueOf(message));
        e.printStackTrace();
    }
    
}
