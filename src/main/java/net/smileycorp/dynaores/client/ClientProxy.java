package net.smileycorp.dynaores.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
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
import net.smileycorp.dynaores.common.CommonProxy;
import net.smileycorp.dynaores.common.Constants;
import net.smileycorp.dynaores.common.RawOres;
import net.smileycorp.dynaores.common.data.OreEntry;
import net.smileycorp.dynaores.common.data.OreHandler;

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
        //register ore models
        RawOres.logInfo("Registering models");
        ModelLoaderRegistry.registerLoader(OreModelLoader.INSTANCE);
        ItemModelMesher mesher = mc.getRenderItem().getItemModelMesher();
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) {
            ModelResourceLocation itemLoc = new ModelResourceLocation(Constants.locStr(entry.getName() + ".raw_ore"));
            ModelLoader.setCustomModelResourceLocation(entry.getItem(), 0, itemLoc);
            //have to manually copy entries to the model mesher because we register our model definitions too late for the game to automatically do it
            mesher.register(entry.getItem(), 0, itemLoc);
            ModelResourceLocation blockLoc = new ModelResourceLocation(Constants.locStr(entry.getName() + "_block.raw_ore"));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(entry.getBlock()), 0, blockLoc);
            StateMapperOreBlock mapper = new StateMapperOreBlock(blockLoc);
            ModelLoader.setCustomStateMapper(entry.getBlock(), mapper);
            mc.getBlockRendererDispatcher().getBlockModelShapes().registerBlockWithStateMapper(entry.getBlock(), mapper);
            //have to manually copy entries to the model mesher because we register our model definitions too late for the game to automatically do it
            mesher.register(Item.getItemFromBlock(entry.getBlock()), 0, blockLoc);
        }
        //register colour handlers
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) {
            ItemColors itemColours = mc.getItemColors();
            itemColours.registerItemColorHandler(OreModelLoader.INSTANCE::getColour, entry.getItem());
            itemColours.registerItemColorHandler(OreModelLoader.INSTANCE::getColour, entry.getBlock());
            mc.getBlockColors().registerBlockColorHandler(OreModelLoader.INSTANCE::getColour, entry.getBlock());
        }
        //refresh textures and models
        FMLClientHandler.instance().refreshResources(VanillaResourceType.TEXTURES, VanillaResourceType.MODELS);
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void stitchTextureEvent(TextureStitchEvent.Pre event) {
        RawOres.logInfo("Stitching Textures");
        OreModelLoader.INSTANCE.stitchTextures(event.getMap());
    }
    
}
