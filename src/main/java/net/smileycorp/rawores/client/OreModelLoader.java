package net.smileycorp.rawores.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.smileycorp.rawores.common.Constants;
import net.smileycorp.rawores.common.RawOres;
import net.smileycorp.rawores.common.data.*;

import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OreModelLoader implements ICustomModelLoader, ISelectiveResourceReloadListener {
    
    public static final OreModelLoader INSTANCE = new OreModelLoader();
    
    protected final Map<String, Integer> colours = Maps.newHashMap();
    protected final List<String> itemTextures = Lists.newArrayList();
    protected final List<String> blockTextures = Lists.newArrayList();
    private final OreModelOverrides overrides = new OreModelOverrides();
    
    public void mapColours() {
        colours.clear();
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) colours.put(entry.getName().toLowerCase(Locale.US), getColourFor(entry));
    }
    
    //find an appropriate colour to tint ores if no texture is provided
    private int getColourFor(OreEntry entry) {
        ItemStack ingot = entry.getIngot();
        //check to see if we forcibly generated the ore
        if (ingot.isEmpty()) return entry.getColour();
        try {
            //get the texture for the corresponding ingot item
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(ingot, null, null)
                    .getQuads(null, null, 0).get(0).getSprite();
            List<Integer> colours = Lists.newArrayList();
            //get all the pixels from the first frame of the sprite with an alpha greater than 0
            for (int [] rows : sprite.getFrameTextureData(0)) for (int colour : rows) if ((colour >> 32 & 0xFF) > 0) colours.add(colour);
            long r = 0, g = 0, b = 0;
            for (int colour : colours) {
                r += (colour >> 16) & 0xFF;
                g += (colour >> 8) & 0xFF;
                b += colour & 0xFF;
            }
            int c = 0xFF000000 + new Color((int) r / colours.size(), (int) g / colours.size(), (int) b / colours.size(), (int) 255).getRGB();
            RawOres.logInfo("Loaded colour " + c + " for " + entry.getName());
            return c;
        } catch (Exception e) {
            RawOres.logError("Error getting colour for " + entry.getName(), e);
            return 0xFFFFFFFF;
        }
    }
    
    public void stitchTextures(TextureMap map) {
        itemTextures.clear();
        Minecraft mc = Minecraft.getMinecraft();
        IResourceManager rm = mc.getResourceManager();
        //register item textures
        map.registerSprite(Constants.loc("items/fallback"));
        for (String name : OreHandler.INSTANCE.getOreNames()) {
            name = name.toLowerCase(Locale.US);
            //use a try here to check if the texture exists
            try {
                rm.getAllResources(Constants.loc("textures/items/" + name + ".png"));
                //register the texture
                map.registerSprite(Constants.loc("items/" + name));
                itemTextures.add(name);
                RawOres.logInfo("Registered texture for " + name);
            } catch (Exception e) {}
            //register block textures
            map.registerSprite(Constants.loc("items/fallback"));
            try {
                rm.getAllResources(Constants.loc("textures/blocks/" + name + ".png"));
                //register the texture
                map.registerSprite(Constants.loc("blocks/" + name));
                blockTextures.add(name);
                RawOres.logInfo("Registered texture for " + name);
            } catch (Exception e) {}
        }
    }
    
    @Override
    public void onResourceManagerReload(IResourceManager manager) {
        mapColours();
    }
    
    @Override
    public boolean accepts(ResourceLocation loc) {
        return loc.getResourcePath().contains(".raw_ore");
    }
    
    @Override
    public IModel loadModel(ResourceLocation location) throws Exception {
        IResourceManager mngr = Minecraft.getMinecraft().getResourceManager();
        try {
            boolean block = location.getResourcePath().contains("block");
            String[] split = location.getResourcePath().split("\\.")[0].split("/");
            String name = block ? "block/" + split[split.length - 1].replace("_block", "") : "items/" + split[split.length - 1];
            try {
                mngr.getAllResources(Constants.loc("textures/" + name + ".png"));
            } catch (Exception e) {
                name = block ? "blocks/fallback" : "items/fallback";
            }
            RawOres.logInfo("Loading model for " + location);
            return block ? ModelLoaderRegistry.getModel(Constants.loc("block/raw_ore_block"))
                    .retexture(ImmutableMap.of("all", Constants.locStr(name))) :
                    new ItemLayerModel(ImmutableList.of(Constants.loc(name)), overrides);
        } catch (Exception e) {
            RawOres.logError("Failed loading model " + location, e);
            return ModelLoaderRegistry.getMissingModel();
        }
    }
    
    public int getColour(ItemStack stack, int index) {
        String name = ((IOreItem)stack.getItem()).getEntry().getName().toLowerCase(Locale.US);
        return !((stack.getItem() instanceof ItemBlockRawOre ? blockTextures : itemTextures).contains(name)) && colours.containsKey(name) ? colours.get(name) : 0xFFFFFFFF;
    }
    
    public int getColour(IBlockState state, IBlockAccess world, BlockPos pos, int index) {
        String name = ((BlockRawOre)state.getBlock()).getEntry().getName().toLowerCase(Locale.US);
        return !(blockTextures.contains(name)) && colours.containsKey(name) ? colours.get(name) : 0xFFFFFFFF;
    }
    
}
