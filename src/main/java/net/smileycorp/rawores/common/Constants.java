package net.smileycorp.rawores.common;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Locale;

public class Constants {
    
    public static final String MODID = "raw_ores";
    public static final String NAME = "Dynamic Raw Ores";
    public static final String VERSION = "1.0.0";
    public static final String DEPENDENCIES = "before:crafttweaker";
    private static final String PACKAGE = "net.smileycorp.rawores.";
    public static final String CLIENT_PROXY = PACKAGE + "client.ClientProxy";
    public static final String SERVER_PROXY = PACKAGE + "common.CommonProxy";
    
    public static String name(String name) {
        return MODID + "." + name.replace("_", "");
    }
    
    public static ResourceLocation loc(String name) {
        return new ResourceLocation(MODID, name.replaceAll("(.)([A-Z])", "$1_$2").toLowerCase(Locale.US));
    }
    
    public static String locStr(String string) {
        return loc(string).toString();
    }
    
    public static ItemStack getStack(String oredict, int count) {
        ItemStack stack = OreDictionary.getOres(oredict).get(0).copy();
        stack.setCount(count);
        return stack;
    }

}
