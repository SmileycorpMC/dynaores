package net.smileycorp.rawores.client;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.smileycorp.rawores.common.OreEntry;
import net.smileycorp.rawores.common.OreHandler;
import net.smileycorp.rawores.common.RawOres;

import java.util.List;

public class OreTextureHandler {
    
    public static final OreTextureHandler INSTANCE = new OreTextureHandler();
    
    public void setUpClient() {
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) entry.setColour(getColourFor(entry));
    }
    
    //find an appropriate colour to tint ores if no texture is provided
    private int getColourFor(OreEntry entry) {
        try {
            //get the texture for the corresponding ingot item
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(entry.getIngot(), null, null).getParticleTexture();
            List<Integer> colours = Lists.newArrayList();
            //get all the pixels from the first frame of the sprite with an alpha greater than 0
            for (int [] rows : sprite.getFrameTextureData(0)) for (int colour : rows) if ((colour >> 32 & 0xFF) > 0) colours.add(colour);
            int r = 0, g = 0, b = 0;
            for (int colour : colours) {
                r += colour >> 16 & 0xFF;
                g += colour >> 8 & 0xFF;
                b += colour & 0xFF;
            }
            return 0xFF000000 + (r / colours.size()) << 16 + (g / colours.size()) << 8 + b / colours.size();
        } catch (Exception e) {
            RawOres.logError("Failed getting texture for ore " + entry.getName(), e);
        }
        return 0xFFFFFFFF;
    }
    
}
