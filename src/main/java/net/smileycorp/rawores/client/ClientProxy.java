package net.smileycorp.rawores.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.rawores.common.*;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }
    
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }
    
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        Minecraft mc = Minecraft.getMinecraft();
        //calculate the tint colours for each registered ore
        OreModelLoader.INSTANCE.mapColours();
        //register ore models
        RawOres.logInfo("Registering models");
        ModelLoaderRegistry.registerLoader(OreModelLoader.INSTANCE);
        ItemModelMesher mesher = mc.getRenderItem().getItemModelMesher();
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) {
            ModelResourceLocation loc = new ModelResourceLocation(Constants.locStr(entry.getName() + ".raw_ore"));
            ModelLoader.setCustomModelResourceLocation(entry.getItem(), 0, loc);
            mesher.register(entry.getItem(), 0, loc);
        }
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) mc.getItemColors().registerItemColorHandler(OreModelLoader.INSTANCE::getColour, entry.getItem());
        //refresh textures and models
        FMLClientHandler.instance().refreshResources(VanillaResourceType.TEXTURES, VanillaResourceType.MODELS);
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void stitchTextureEvent(TextureStitchEvent.Pre event) {
        RawOres.logInfo("Stitching Textures");
        OreModelLoader.INSTANCE.stitchTextures(event.getMap());
    }
    
}
