package net.smileycorp.dynaores.common;

import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class ConfigHandler {
    
    public static boolean canFortune = true;
    public static boolean invertBlacklist = true;
    
    private static String[] blacklist = {
            "AncientDebris"
    };
    
    private static String[] ignoredWords = {
            "Endstone",
            "Netherrack"
    };
    
    private static String[] otherNames = {
            "Aluminium-Aluminum",
            "Mythril-Mithril",
            "Adamantite-Adamantium",
            "Wolfram-Tungsten",
            "Chromium-Chrome",
            "Bauxite-Aluminum",
            "Mercury-Quicksilver"
    };
    
    public static String[] detectedMaterials = {
            "ingot"
    };
    
    public static String[] denseOresStr = {
            "Dense-2",
            "Nether-2",
            "End-2"
    };
    
    private static String[] forceGenerate = {};
    
    private static final Map<String, Double> denseOres = Maps.newHashMap();
    private static final Map<String, String> nameMap = Maps.newHashMap();
    
    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        try{
            config.load();
            blacklist = config.get("general", "blacklist", blacklist, "Which ores shouldn't have variants generated for them?").getStringList();
            canFortune = config.get("general", "canFortune", true, "Can ores be fortuned to drop more raw ores?").getBoolean();
            invertBlacklist = config.get("general", "invertBlacklist", false, "Whether the blacklist should instead be read as a whitelist, only generating ores for the given names.").getBoolean();
            ignoredWords = config.get("general", "ignoredWords", ignoredWords, "Words to be ignored in oredictionaries to match variants (e.g. making nether and end variants drop their corresponding ore)?").getStringList();
            otherNames = config.get("general", "otherNames", otherNames, "Ores that are counted as the same types as each other separated by \"-\", e.g Magnetite-Iron will make ores with the name oreMagnetite be treated as oreIron for the purposes of raw ore generation and dropping.").getStringList();
            detectedMaterials = config.get("general", "detectedMaterials", detectedMaterials, "Which types of material are detected?, (by default, just ingots)").getStringList();
            denseOresStr = config.get("general", "denseOres", denseOresStr, "Ore dictionary names that are used to refer to dense ores and the drop multiplier seperated by \"-\", e.g. Dense-2 will make ores with \"Dense\" in their ore dictionarry drop twice as much raw ore").getStringList();
        } catch(Exception e) {} finally {
            if (config.hasChanged()) config.save();
        }
    }
    
    public static boolean isBlacklisted(String name) {
        return invertBlacklist ^ (Arrays.stream(blacklist).anyMatch(s -> s.toLowerCase(Locale.US).equals(name.toLowerCase(Locale.US))));
    }
    
    public static String format(String name) {
        if (nameMap.containsKey(name)) return nameMap.get(name);
        String newName = name;
        for (String word : ignoredWords) if (newName.contains(word)) newName = newName.replace(word, "");
        for (String str : denseOresStr) {
            String[] split = str.split("-");
            if (newName.contains(split[0])) {
                try {
                    double amount = Double.valueOf(split[1]);
                    denseOres.put(name, amount);
                    DynaOresLogger.logInfo("Added dense ore " + name + " with amount " + amount);
                } catch (Exception e) {
                    DynaOresLogger.logError("Error adding dense ore " + str, e);
                }
                newName = newName.replace(split[0], "");
            }
        }
        for (String str : otherNames) {
            String[] split = str.split("-");
            try {
                if (split[0].equals(newName)) newName = split[1];
            } catch (Exception e) {}
        }
        nameMap.put(name, newName);
        return newName;
    }
    
    public static double getMultiplier(String name) {
        name = name.replace("ore", "");
        for (String material : ConfigHandler.detectedMaterials) name = name.replace(material, "");
        return denseOres.containsKey(name) ? denseOres.get(name) : 1;
    }
    
}
