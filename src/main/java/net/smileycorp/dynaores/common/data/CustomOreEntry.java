package net.smileycorp.dynaores.common.data;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import java.util.function.Supplier;

public class CustomOreEntry extends OreEntry {

    private final int colour;
    private final Supplier<ItemStack> materialGetter;
    private ItemStack material;

    public CustomOreEntry(String name, int colour, Supplier<ItemStack> materialGetter) {
        super(name);
        this.colour = colour;
        this.materialGetter = materialGetter;
    }

    @Override
    public String getLocalizedName() {
        String key = getTranslationKey();
        //check if translation key exists, if not format the oredict name
        if (I18n.canTranslate(key)) return I18n.translateToLocal(key);
        if (material != null) if (material.isEmpty()) return material.getDisplayName();
        return name.replaceAll("(.)([A-Z])", "$1 $2");
    }

    @Override
    public ItemStack getMaterial() {
        if (material == null) material = materialGetter.get();
        return material;
    }
    
    @Override
    public int getColour() {
        return colour;
    }

    @Override
    public boolean isCustom() {
        return true;
    }
    
}
