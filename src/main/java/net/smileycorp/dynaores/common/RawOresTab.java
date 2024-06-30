package net.smileycorp.dynaores.common;

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
    
    //dynamic item tab, switch icon every 5 seconds
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (items == null) {
            //initialise our stored item list, so we can pull from them later without iterating through the whole item registry again
            items = NonNullList.create();
            displayAllRelevantItems(items);
        }
        //mark the tab to refresh the tick after it last refreshed
        //we need to use a variable so it doesn't quickly cycle between item for a tick when the item changes
        if (!needsRefresh && Minecraft.getMinecraft().world.getTotalWorldTime() % 80 == 1) needsRefresh = true;
        //change the item every 80 ticks
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
