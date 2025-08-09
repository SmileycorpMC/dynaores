package net.smileycorp.dynaores.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.dynaores.common.CommonProxy;
import net.smileycorp.dynaores.common.Constants;
import net.smileycorp.dynaores.common.DynaOresLogger;
import net.smileycorp.dynaores.common.data.OreCacheLoader;
import net.smileycorp.dynaores.common.data.OreEntry;
import net.smileycorp.dynaores.common.data.OreHandler;

public class ClientProxy extends CommonProxy {

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        if (OreCacheLoader.INSTANCE.isActive()) return;
        Minecraft mc = Minecraft.getMinecraft();
        //register ore models
        DynaOresLogger.logInfo("Registering models");
        ModelLoaderRegistry.registerLoader(OreModelLoader.INSTANCE);
        ItemModelMesher mesher = mc.getRenderItem().getItemModelMesher();
        ItemColors itemColours = mc.getItemColors();
        BlockColors blockColors = mc.getBlockColors();
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) {
            ModelResourceLocation itemLoc = new ModelResourceLocation(Constants.locStr(entry.getName() + ".raw_ore"));
            ModelLoader.setCustomModelResourceLocation(entry.getItem(), 0, itemLoc);
            //have to manually copy entries to the model mesher because we register our model definitions too late for the game to automatically do it
            mesher.register(entry.getItem(), 0, itemLoc);
            itemColours.registerItemColorHandler(OreModelLoader.INSTANCE::getColour, entry.getItem());
            if (entry.getBlock() != null) {
                ModelResourceLocation blockLoc = new ModelResourceLocation(Constants.locStr(entry.getName() + "_block.raw_ore"));
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(entry.getBlock()), 0, blockLoc);
                StateMapperOreBlock mapper = new StateMapperOreBlock(blockLoc);
                ModelLoader.setCustomStateMapper(entry.getBlock(), mapper);
                mc.getBlockRendererDispatcher().getBlockModelShapes().registerBlockWithStateMapper(entry.getBlock(), mapper);
                //have to manually copy entries to the model mesher because we register our model definitions too late for the game to automatically do it
                mesher.register(Item.getItemFromBlock(entry.getBlock()), 0, blockLoc);
                itemColours.registerItemColorHandler(OreModelLoader.INSTANCE::getColour, entry.getBlock());
                blockColors.registerBlockColorHandler(OreModelLoader.INSTANCE::getColour, entry.getBlock());
            }
        }
        //refresh textures and models
        FMLClientHandler.instance().refreshResources(VanillaResourceType.TEXTURES, VanillaResourceType.MODELS);
    }

    @SubscribeEvent
    public void onModelRegister(ModelRegistryEvent event) {
        if (!OreCacheLoader.INSTANCE.isActive()) return;
        //register ore models
        DynaOresLogger.logInfo("Registering models");
        ModelLoaderRegistry.registerLoader(OreModelLoader.INSTANCE);
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) {
            ModelResourceLocation itemLoc = new ModelResourceLocation(Constants.locStr(entry.getName() + ".raw_ore"));
            ModelLoader.setCustomModelResourceLocation(entry.getItem(), 0, itemLoc);
            if (entry.getBlock() != null) {
                ModelResourceLocation blockLoc = new ModelResourceLocation(Constants.locStr(entry.getName() + "_block.raw_ore"));
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(entry.getBlock()), 0, blockLoc);
                StateMapperOreBlock mapper = new StateMapperOreBlock(blockLoc);
                ModelLoader.setCustomStateMapper(entry.getBlock(), mapper);
            }
        }
    }
    
    @SubscribeEvent
    public void onBlockColorRegister(ColorHandlerEvent.Block event) {
        if (!OreCacheLoader.INSTANCE.isActive()) return;
        BlockColors blockColors = event.getBlockColors();
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) {
            if (entry.getBlock() != null) {
                blockColors.registerBlockColorHandler(OreModelLoader.INSTANCE::getColour, entry.getBlock());
            }
        }
    }
    
    @SubscribeEvent
    public void onItemColorRegister(ColorHandlerEvent.Item event) {
        if (!OreCacheLoader.INSTANCE.isActive()) return;
        ItemColors itemColours = event.getItemColors();
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) {
            itemColours.registerItemColorHandler(OreModelLoader.INSTANCE::getColour, entry.getItem());
            if (entry.getBlock() != null) {
                itemColours.registerItemColorHandler(OreModelLoader.INSTANCE::getColour, entry.getBlock());
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public void stitchTextureEvent(TextureStitchEvent.Pre event) {
        DynaOresLogger.logInfo("Stitching Textures");
        OreModelLoader.INSTANCE.stitchTextures(event.getMap());
    }
    
}
