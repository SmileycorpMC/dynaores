package net.smileycorp.dynaores.common.data;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.smileycorp.dynaores.client.OreModelLoader;

public class GeneratedOreEntry extends OreEntry {
    
    private final ItemStack material;
    private int colour = 0;
    
    public GeneratedOreEntry(String name, ItemStack material) {
        super(name);
        this.material = material;
        GameRegistry.addSmelting(item, material, 0.5f);
    }
    
    @Override
    public String getLocalizedName() {
        String key = getTranslationKey();
        //check if translation key exists, if not format the oredict name
        return I18n.canTranslate(key) ? I18n.translateToLocal(key) : material.getDisplayName();
    }
    
    @Override
    public int getColour() {
        if (colour == 0)  colour = OreModelLoader.INSTANCE.getColourFor(material, this);
        return colour;
    }
    
    public void refresh() {
        colour = 0;
    }
    
}
