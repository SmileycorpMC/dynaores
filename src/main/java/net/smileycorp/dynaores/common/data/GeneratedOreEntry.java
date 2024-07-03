package net.smileycorp.dynaores.common.data;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.smileycorp.dynaores.client.OreModelLoader;

public class GeneratedOreEntry extends OreEntry {
    
    private final ItemStack ingot;
    private int colour = 0;
    
    public GeneratedOreEntry(String name, ItemStack ingot) {
        super(name);
        this.ingot = ingot;
        GameRegistry.addSmelting(item, ingot, 0.5f);
    }
    
    @Override
    public String getLocalizedName() {
        return ingot.getDisplayName();
    }
    
    @Override
    public int getColour() {
        if (colour == 0)  colour = OreModelLoader.INSTANCE.getColourFor(ingot, name);
        return colour;
    }
    
    public void refresh() {
        colour = 0;
    }
    
}
