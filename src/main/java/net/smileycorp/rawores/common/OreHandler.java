package net.smileycorp.rawores.common;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OreHandler {
    
    public static final OreHandler INSTANCE = new OreHandler();
    
    private final Map<String, OreEntry> entries = Maps.newHashMap();
    
    public void buildOreProperties() {
        //find all ore dictionary entries that match the pattern ore*****
        for (String ore : OreDictionary.getOreNames()) {
            if (!ore.contains("ore")) continue;
            String name = ore.replace("ore", "");
            //make sure we don't accidentally register an entry twice (somehow)
            if (entries.containsKey(name)) continue;
            List<ItemStack> ores = OreDictionary.getOres(ore);
            //check that one of the ores is a block so we don't add raw items for things like knightmetal
            if (!hasBlock(ores)) continue;
            //check if there is a corresponding ingot***** to filter out nonmetals like gems and dusts
            String ingot = ore.replace("ore", "ingot");
            if (!OreDictionary.doesOreNameExist(ingot)) continue;
            List<ItemStack> ingots = OreDictionary.getOres(ingot);
            if (ingots.isEmpty()) continue;
            OreEntry entry = new OreEntry(name, ores, ingots.get(0));
            entries.put(name, entry);
            //registering in post init is usually a bad idea, but we have to do all our registering after other mods have loaded
            ForgeRegistries.ITEMS.register(entry.getItem());
            OreDictionary.registerOre(ore, new ItemStack(entry.getItem()));
        }
        RawOres.logInfo("Detected ore types " + entries.keySet());
    }
    
    //check if the registered ores contain a block to prevent creating unnecessary raw items
    private boolean hasBlock(List<ItemStack> ores) {
        for (ItemStack ore : ores) if (ore.getItem() instanceof ItemBlock) return true;
        return false;
    }
    
    public Collection<OreEntry> getOres() {
        return entries.values();
    }
    
    public OreEntry getEntry(ItemStack stack) {
        for (OreEntry entry : entries.values()) if (entry.contains(stack)) return entry;
        return null;
    }
    
    public OreEntry getEntry(String ore) {
        return entries.get(ore);
    }
    
}
