package net.smileycorp.rawores.common;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class ConfigHandler {
    
    public static boolean canFortune = true;
    
    private static String[] blacklist = {
        "AncientDebris"
    };
    
    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        try{
            config.load();
            blacklist = config.get("general", "blacklist", blacklist, "Which ores shouldn't have variants generated for them?").getStringList();
            canFortune = config.get("general", " canFortune", true, "Can ores be fortuned to drop more raw ores?").getBoolean();
        } catch(Exception e) {} finally {
            if (config.hasChanged()) config.save();
        }
    }
    
    public static boolean isBlacklisted(String name) {
        return (Arrays.stream(blacklist).anyMatch(s -> s.toLowerCase(Locale.US).equals(name.toLowerCase(Locale.US))));
    }

}
