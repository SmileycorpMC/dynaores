package net.smileycorp.dynaores.common.data;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.dynaores.common.ConfigHandler;
import net.smileycorp.dynaores.common.DynaOresLogger;
import net.smileycorp.dynaores.common.item.IOreItem;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OreHandler {
    
    public static final OreHandler INSTANCE = new OreHandler();
    
    private final Map<String, OreEntry> entries = Maps.newHashMap();
    private final Map<String, OreEntry> dupeEntries = Maps.newHashMap();
    
    //try to register the given oredictionary
    public void tryRegister(String ore, ItemStack stack) {
        //registering our own items causes recursion here, cancel it preemptively before we start doing anything else
        //also we don't want duplicate variants
        if (stack.getItem() instanceof IOreItem) return;
        //is it an ore we are registering
        String type = "";
        if (!ore.contains("ore")) {
            for (String material : ConfigHandler.detectedMaterials) if (ore.contains(material)) {
                type = material;
                break;
            }
            if (type.isEmpty()) return;
        }
        boolean isMaterial = type.isEmpty();
        String s = ore.replace(isMaterial ? type : "ore", "");
        //make sure we don't accidentally register an entry twice
        if (dupeEntries.containsKey(s)) return;
        String name = format(s);
        if (entries.containsKey(name)) {
            dupeEntries.put(s, entries.get(name));
            return;
        }
        if (ConfigHandler.isBlacklisted(name)) return;
        List<ItemStack> ores = OreDictionary.getOres(isMaterial ? ore.replace(type, "ore") : ore);
        //check that one of the ores is a block, so we don't add raw items for things like knightmetal
        if (!hasBlock(ores)) return;
        //check if there is a corresponding ingot***** to filter out nonmetals like gems and dusts
        List<ItemStack> materials = null;
        if (!isMaterial) {
            for (String material : ConfigHandler.detectedMaterials) {
                String typeName = ore.replace("ore", type);
                if (!OreDictionary.doesOreNameExist(typeName)) continue;
                materials = OreDictionary.getOres(typeName);
                if (materials.isEmpty()) continue;
                type = material;
            }
            if (type.isEmpty()) return;
        }
        OreEntry entry = new GeneratedOreEntry(name, isMaterial ? stack : materials.get(0));
        entries.put(name, entry);
        //put a copy in the entry map if the name is different from the entry, so we don't have to keep iterating through the format list
        if (!s.equals(name)) dupeEntries.put(s, entry);
        DynaOresLogger.logInfo("Registered ore " + name);
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
        ore = ore.replace("ore", "");
        return dupeEntries.containsKey(ore) ? dupeEntries.get(ore) : entries.get(ore);
    }
    
    private String format(String ore) {
        return ConfigHandler.format(ore.replace("ore", ""));
    }
    
    public Collection<String> getOreNames() {
        return entries.keySet();
    }
    
}
