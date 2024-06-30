package net.smileycorp.rawores.common;

import net.minecraft.util.ResourceLocation;

public class Constants {
    
    public static final String MODID = "raw_ores";
    public static final String NAME = "Raw Ores";
    public static final String VERSION = "1.0.0";
    public static final String DEPENDENCIES = "before:crafttweaker";
    private static final String PACKAGE = "net.smileycorp.rawores.";
    public static final String CLIENT_PROXY = PACKAGE + "client.ClientProxy";
    public static final String SERVER_PROXY = PACKAGE + "common.CommonProxy";
    
    public static String name(String name) {
        return name(MODID, name);
    }
    
    public static String name(String modid, String name) {
        return modid + "." + name.replace("_", "");
    }
    
    public static ResourceLocation loc(String name) {
        return new ResourceLocation(MODID, name.toLowerCase());
    }
    
    public static String locStr(String string) {
        return loc(string).toString();
    }

}
