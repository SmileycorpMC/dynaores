package net.smileycorp.rawores.common.data;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class OreEntry {
    
    private final String name;
    private final List<ItemStack> ores;
    private final ItemStack ingot;
    private final ItemRawOre item;
    
    public OreEntry(String name, List<ItemStack> ores, ItemStack ingot) {
        this.name = name;
        this.ores = ores;
        this.ingot = ingot;
        this.item = new ItemRawOre(this);
        GameRegistry.addSmelting(item, ingot, 0.1f);
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
    
    public boolean contains(ItemStack ore) {
        return ores.contains(ore);
    }
    
}
