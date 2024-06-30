package net.smileycorp.rawores.common.data;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.smileycorp.rawores.client.OreModelLoader;

public class GeneratedOreEntry extends OreEntry {
    
    private final ItemStack ore, ingot;
    private int colour = -1;
    
    public GeneratedOreEntry(String name, ItemStack ore, ItemStack ingot) {
        super(name);
        this.ore = ore;
        this.ingot = ingot;
        GameRegistry.addSmelting(item, ingot, 0.1f);
    }
    
    @Override
    public String getLocalizedName() {
        return ore.getDisplayName();
    }
    
    @Override
    public int getColour() {
        if (colour == -1)  colour = OreModelLoader.INSTANCE.getColourFor(ingot, name);
        return colour;
    }
    
    public void refresh() {
        colour = -1;
    }
    
}