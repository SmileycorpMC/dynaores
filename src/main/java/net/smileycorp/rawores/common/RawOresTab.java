package net.smileycorp.rawores.common;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class RawOresTab extends CreativeTabs {
    
    private final Random rand = new Random();
    private ItemStack stack;
    private NonNullList<ItemStack> items;
    private boolean needsRefresh = true;
    
    public RawOresTab() {
        super(Constants.name("tab"));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (items == null) {
            items = NonNullList.create();
            displayAllRelevantItems(items);
        }
        if (!needsRefresh && Minecraft.getMinecraft().world.getTotalWorldTime() % 80 == 1) needsRefresh = true;
        if (stack == null || (needsRefresh && Minecraft.getMinecraft().world.getTotalWorldTime() % 80 == 0)) {
            stack = items.get(rand.nextInt(items.size()));
            needsRefresh = false;
        }
        return stack;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        return getIconItemStack();
    }
    
}
