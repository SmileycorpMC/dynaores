package net.smileycorp.rawores.common.data;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.rawores.common.ConfigHandler;
import net.smileycorp.rawores.common.RawOres;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OreHandler {
    
    public static final OreHandler INSTANCE = new OreHandler();
    
    private final Map<String, OreEntry> entries = Maps.newHashMap();
    
    //generate all ore entries used by the mod
    public void buildOreProperties() {
        //find all ore dictionary entries that match the pattern ore*****
        for (String ore : OreDictionary.getOreNames()) {
            if (!ore.contains("ore")) continue;
            String name = format(ore);
            //make sure we don't accidentally register an entry twice (somehow)
            if (entries.containsKey(name) || ConfigHandler.isBlacklisted(name)) continue;
            List<ItemStack> ores = OreDictionary.getOres(ore);
            //check that one of the ores is a block, so we don't add raw items for things like knightmetal
            if (!hasBlock(ores)) continue;
            //check if there is a corresponding ingot***** to filter out nonmetals like gems and dusts
            String ingot = ore.replace("ore", "ingot");
            if (!OreDictionary.doesOreNameExist(ingot)) continue;
            List<ItemStack> ingots = OreDictionary.getOres(ingot);
            if (ingots.isEmpty()) continue;
            OreEntry entry = new OreEntry(name, ores.get(0).getUnlocalizedName().replace(".name", "") + ".name", ingots.get(0));
            entries.put(name, entry);
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
    
    public OreEntry getEntry(String ore) {
        return entries.get(format(ore));
    }
    
    private String format(String ore) {
        return ConfigHandler.format(ore.replace("ore", ""));
    }
    
    public Collection<String> getOreNames() {
        return entries.keySet();
    }
    
}
