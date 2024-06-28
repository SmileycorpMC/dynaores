package net.smileycorp.rawores.common;

import net.minecraft.item.ItemStack;

import java.util.Dictionary;
import java.util.List;

public class OreEntry {
    
    private final String name;
    private final List<ItemStack> ores;
    private final ItemStack ingot;
    private final ItemRawOre item;
    private int colour;
    
    public OreEntry(String name, List<ItemStack> ores, ItemStack ingot) {
        this.name = name;
        this.ores = ores;
        this.ingot = ingot;
        this.item = new ItemRawOre(this);
    }
    
    public String getName() {
        return name;
    }
    
    public List<ItemStack> getOres() {
        return ores;
    }
    
    public ItemStack getIngot() {
        return ingot;
    }
    
    public ItemRawOre getItem() {
        return item;
    }
    
    public int getColour() {
        return colour;
    }
    
    public void setColour(int colour) {
        this.colour = colour;
    }
    
    public boolean contains(ItemStack ore) {
        return ores.contains(ore);
    }
    
}
