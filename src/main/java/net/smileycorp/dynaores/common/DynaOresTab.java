package net.smileycorp.dynaores.common;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.dynaores.common.data.OreEntry;
import net.smileycorp.dynaores.common.data.OreHandler;

import java.util.Random;

public class DynaOresTab extends CreativeTabs {
    
    private final Random rand = new Random();
    private ItemStack stack = new ItemStack(Blocks.IRON_ORE);
    private NonNullList<ItemStack> items;
    private boolean needsRefresh = true;
    
    public DynaOresTab() {
        super(Constants.name("tab"));
        setBackgroundImageName("item_search.png");
    }
    
    //dynamic item tab, switch icon every 5 seconds
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (items == null) {
            //initialise our stored item list, so we can pull from them later without iterating through the whole item registry again
            items = NonNullList.create();
            NonNullList<ItemStack> blocks = NonNullList.create();
            for (OreEntry entry : OreHandler.INSTANCE.getOres()) {
                items.add(new ItemStack(entry.getItem()));
                if (entry.getBlock() != null) blocks.add(new ItemStack(entry.getBlock()));
            }
            if (!blocks.isEmpty()) for (ItemStack block : blocks) if (block != null) items.add(block);
            if (!items.isEmpty()) stack = items.get(rand.nextInt(items.size()));
            needsRefresh = false;
        }
        if (items.isEmpty()) return stack;
        //mark the tab to refresh the tick after it last refreshed
        //we need to use a variable, so it doesn't quickly cycle between item for a tick when the item changes
        long time = Minecraft.getMinecraft().world.getTotalWorldTime();
        if (!needsRefresh && time % 80 == 1) needsRefresh = true;
        //change the item every 80 ticks
        if (needsRefresh && time % 80 == 0) {
            stack = items.get(rand.nextInt(items.size()));
            needsRefresh = false;
        }
        return stack;
    }

    @SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> items) {
        if (this.items != null) {
            for (ItemStack stack : this.items) if (stack != null) items.add(stack);
            return;
        }
        super.displayAllRelevantItems(items);
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
        return getIconItemStack();
    }
    
}
