package net.smileycorp.dynaores.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.smileycorp.dynaores.common.Constants;
import net.smileycorp.dynaores.common.DynaOresLogger;
import net.smileycorp.dynaores.common.block.BlockRawOre;
import net.smileycorp.dynaores.common.data.OreEntry;
import net.smileycorp.dynaores.common.data.OreHandler;
import net.smileycorp.dynaores.common.item.IOreItem;
import net.smileycorp.dynaores.common.item.ItemBlockRawOre;

import java.awt.*;
import java.util.List;
import java.util.Locale;

public class OreModelLoader implements ICustomModelLoader, ISelectiveResourceReloadListener {
    
    public static final OreModelLoader INSTANCE = new OreModelLoader();
    
    protected final List<String> itemTextures = Lists.newArrayList();
    protected final List<String> blockTextures = Lists.newArrayList();
    private final OreModelOverrides overrides = new OreModelOverrides();
    
    public int getColourFor(ItemStack stack, OreEntry entry) {
        String name = entry.getName().toLowerCase(Locale.US);
        if (itemTextures.contains(name)) {
            if (blockTextures.contains(name)) return 0xFFFFFFFF;
            stack = new ItemStack(entry.getItem());
        }
        else if (blockTextures.contains(name)) stack = new ItemStack(entry.getBlock());
        return getColourFor(stack).getRGB();
    }
    
    //find an appropriate colour to tint ores if no texture is provided
    public Color getColourFor(ItemStack stack) {
        try {
            //get the texture for the corresponding ingot item
            TextureAtlasSprite sprite;
            //if the item is a block our standard method doesn't work, so we have to jank a texture;
            if (stack.getItem() instanceof ItemBlock) {
                Minecraft mc = Minecraft.getMinecraft();
                IBlockState state = ((ItemBlock) stack.getItem()).getBlock().getStateForPlacement(mc.world,
                        new BlockPos(0,0,0), EnumFacing.UP, 0, 0, 0, stack.getMetadata(), mc.player);
                List<BakedQuad> quads = mc.getBlockRendererDispatcher().getModelForState(state).getQuads(state, EnumFacing.NORTH, 0);
                if (quads.isEmpty()) return Color.WHITE;
                sprite = quads.get(0).getSprite();
            } else sprite = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, null, null)
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
            Color c = new Color((int) r / colours.size(), (int) g / colours.size(), (int) b / colours.size(), 255);
            DynaOresLogger.logInfo("Found colour " + c + " for " + stack);
            return c;
        } catch (Exception e) {
            DynaOresLogger.logError("Error getting colour for " + stack, e);
            return Color.WHITE;
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
                DynaOresLogger.logInfo("Registered item texture for " + name);
            } catch (Exception e) {}
            //register block textures
            map.registerSprite(Constants.loc("items/fallback"));
            try {
                rm.getAllResources(Constants.loc("textures/blocks/" + name + ".png"));
                //register the texture
                map.registerSprite(Constants.loc("blocks/" + name));
                blockTextures.add(name);
                DynaOresLogger.logInfo("Registered block texture for " + name);
            } catch (Exception e) {}
        }
    }
    
    @Override
    public void onResourceManagerReload(IResourceManager manager) {
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) entry.refresh();
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
            String name = block ? "blocks/" + split[split.length - 1].replace("_block", "") : "items/" + split[split.length - 1];
            try {
                mngr.getAllResources(Constants.loc("textures/" + name + ".png"));
            } catch (Exception e) {
                name = block ? "blocks/fallback" : "items/fallback";
            }
            DynaOresLogger.logInfo("Loading model for " + location);
            return block ? ModelLoaderRegistry.getModel(Constants.loc("block/raw_ore_block"))
                    .retexture(ImmutableMap.of("all", Constants.locStr(name))) :
                    new ItemLayerModel(ImmutableList.of(Constants.loc(name)), overrides);
        } catch (Exception e) {
            DynaOresLogger.logError("Failed loading model " + location, e);
            return ModelLoaderRegistry.getMissingModel();
        }
    }
    
    public int getColour(ItemStack stack, int index) {
        OreEntry entry = ((IOreItem) stack.getItem()).getEntry();
        String name = entry.getName().toLowerCase(Locale.US);
        return !(stack.getItem() instanceof ItemBlockRawOre ? blockTextures : itemTextures).contains(name) ? entry.getColour() : 0xFFFFFFFF;
    }
    
    public int getColour(IBlockState state, IBlockAccess world, BlockPos pos, int index) {
        OreEntry entry = ((BlockRawOre) state.getBlock()).getEntry();
        String name = entry.getName().toLowerCase(Locale.US);
        return !blockTextures.contains(name) ? entry.getColour() : 0xFFFFFFFF;
    }
    
}
