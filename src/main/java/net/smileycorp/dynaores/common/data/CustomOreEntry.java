package net.smileycorp.dynaores.common.data;

import net.minecraft.util.text.translation.I18n;

import java.util.Locale;

public class CustomOreEntry extends OreEntry {
    
    private final int colour;
    
    public CustomOreEntry(String name, int colour) {
        super(name);
        this.colour = colour;
    }
    
    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal("material.raw_ores." + name.toLowerCase(Locale.US));
    }
    
    @Override
    public int getColour() {
        return colour;
    }
    
}
