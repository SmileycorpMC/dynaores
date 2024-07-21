package net.smileycorp.dynaores.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.smileycorp.dynaores.common.data.OreEntry;
import net.smileycorp.dynaores.common.data.OreHandler;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClientCommandHandler {
    
    public static void copyToClipboard(String message) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(message);
        clipboard.setContents(selection, selection);
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Copied \"" + message + "\" to clipboard."));
    }
    
    public static void sendHeldItemColour() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty()) {
            player.sendMessage(new TextComponentString("Unable to run /dynaores getColour, as player is not holding an item"));
            return;
        }
        Color color = OreModelLoader.INSTANCE.getColourFor(stack);
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        String rgb = "0x" + String.format("%H", ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF) << 0));
        player.sendMessage(getCopyableMessage("Average colour of " + stack.getDisplayName() + " is " +
                String.format("{%d, %d, %d}", r, g, b) + " RBG - " + rgb + " HEX", rgb));
    }
    
    public static void sendLangKey(String ore) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        OreEntry entry = OreHandler.INSTANCE.getEntry(ore);
        if (entry == null) {
            player.sendMessage(new TextComponentString("Unable to run /dynaores getLang " + ore + ", as ore entry does not exist"));
            return;
        }
        String key = entry.getTranslationKey();
        player.sendMessage(getCopyableMessage("Lang key of ore " + ore + " is " + key, key));
    }
    
    public static void sendTextureLocation(String ore, boolean block) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        OreEntry entry = OreHandler.INSTANCE.getEntry(ore);
        if (entry == null) {
            player.sendMessage(new TextComponentString("Unable to run /dynaores getTextureLoc " + ore + ", as ore entry does not exist"));
            return;
        }
        String loc = "assets/dynaores/textures/" + (block ? "blocks" : "items") + "/" + entry.getNameLowercase() + ".png";
        player.sendMessage(getCopyableMessage("Texture location of raw " + ore + (block ? "block" : "") + " is " + loc, loc));
    }
    
    public static ITextComponent getCopyableMessage(String message, String clipboardMessage) {
        return new TextComponentString(message).setStyle(new Style()
                .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dynaores copy " + clipboardMessage))
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Click to copy \"" + clipboardMessage + "\" to clipboard."))));
    }
}
