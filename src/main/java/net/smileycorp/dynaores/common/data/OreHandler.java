package net.smileycorp.dynaores.common.data;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.dynaores.common.ConfigHandler;
import net.smileycorp.dynaores.common.CraftTweakerIntegration;
import net.smileycorp.dynaores.common.DynaOresLogger;
import net.smileycorp.dynaores.common.item.IOreItem;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OreHandler {
    
    public static final OreHandler INSTANCE = new OreHandler();
    
    private final Map<String, OreEntry> entries = Maps.newHashMap();
    private final Map<String, OreEntry> dupeEntries = Maps.newHashMap();
    
    public void registerConfigOres() {
        Arrays.stream(ConfigHandler.customOres).forEach(this::tryRegisterCustom);
    }

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
        boolean isMaterial = !type.isEmpty();
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
                String typeName = ore.replace("ore", material);
                if (!OreDictionary.doesOreNameExist(typeName)) continue;
                materials = OreDictionary.getOres(typeName);
                if (materials.isEmpty()) continue;
                type = material;
                break;
            }
            if (type.isEmpty()) return;
        }
        if (materials != null && materials.isEmpty()) return;
        OreEntry entry = new GeneratedOreEntry(name, isMaterial ? stack : materials.get(0));
        entries.put(name, entry);
        //put a copy in the entry map if the name is different from the entry, so we don't have to keep iterating through the format list
        if (!s.equals(name)) dupeEntries.put(s, entry);
        DynaOresLogger.logInfo("Registered ore " + name);
        if (Loader.isModLoaded("crafttweaker") && CraftTweakerIntegration.isRunning()) CraftTweakerIntegration.refreshItems();
    }

    //register a custom ore from the config or cache
    public void tryRegisterCustom(String entry) {
        if (entry.isEmpty()) return;
        String[] split = entry.split("-");
        String name = split[0];
        int colour = 0xFFFFFFFF;
        if (split.length > 1) {
            try {
                colour = Integer.decode(split[1]);
            } catch (Exception e) {}
        }
        entries.put(name, new CustomOreEntry(name, colour, split.length > 2 ?
                () -> getMaterial(split[2]) : () -> getOredictMaterial(name)));
        DynaOresLogger.logInfo("Registered ore " + name);
    }

    private ItemStack getMaterial(String name) {
        NBTTagCompound nbt = null;
        if (name.contains("{")) {
            String nbtstring = name.substring(name.indexOf("{"));
            name = name.substring(0, name.indexOf("{"));
            try {
                NBTTagCompound parsed = JsonToNBT.getTagFromJson(nbtstring);
                if (parsed != null) nbt = parsed;
            } catch (Exception e) {
                DynaOresLogger.logError("Error parsing nbt for item " + name + " " + e.getMessage(), e);
            }
        }
        String[] nameSplit = name.split(":");
        if (nameSplit.length >= 2) {
            ResourceLocation loc = new ResourceLocation(nameSplit[0], nameSplit[1]);
            int meta;
            try {
                meta = nameSplit.length > 2 ? Integer.parseInt(nameSplit[2]) : 0;
            } catch (Exception e) {
                meta = 0;
                DynaOresLogger.logError("Entry" + name + " has a non integer metadata value", e);
            }
            if (ForgeRegistries.ITEMS.containsKey(loc)) {
                ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(loc), 1, meta);
                if (nbt != null) stack.setTagCompound(nbt);
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private ItemStack getOredictMaterial(String name) {
        NonNullList<ItemStack> ores = OreDictionary.getOres("ingot" + name);
        return ores.isEmpty() ? ItemStack.EMPTY : ores.get(0);
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
        for (String material : ConfigHandler.detectedMaterials) ore = ore.replace(material, "");
        return dupeEntries.containsKey(ore) ? dupeEntries.get(ore) : entries.get(ore);
    }
    
    private String format(String ore) {
        return ConfigHandler.format(ore.replace("ore", ""));
    }
    
    public Collection<String> getOreNames() {
        return entries.keySet();
    }
    
}
