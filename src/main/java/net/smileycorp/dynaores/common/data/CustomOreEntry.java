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
        String key = "material.dynaores." + name.toLowerCase(Locale.US);
        //check if translation key exists, if not format the oredict name
        return I18n.canTranslate(key) ? I18n.translateToLocal(key) : name.replaceAll("(.)([A-Z])", "$1 $2");
    }
    
    @Override
    public int getColour() {
        return colour;
    }
    
}
