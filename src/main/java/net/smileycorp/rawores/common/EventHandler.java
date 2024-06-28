package net.smileycorp.rawores.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.block.BlockUtils;

import java.util.List;

public class EventHandler {
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void dropItem(BlockEvent.HarvestDropsEvent event) {
        List<ItemStack> drops = event.getDrops();
        IBlockState state = event.getState();
        try {
            ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
            String ore = getOre(stack);
            if (ore == null) return;
            OreEntry entry = OreHandler.INSTANCE.getEntry(ore.replace("ore", ""));
            if (entry == null) return;
            for (int i = 0; i < drops.size(); i++) {
                ItemStack drop = drops.get(i);
                if (!ItemStack.areItemsEqual(stack, drop)) continue;
                drops.set(i, new ItemStack(entry.getItem(), BlockUtils.getFortune(event.getFortuneLevel(), event.getHarvester().getRNG())));
            }
        } catch (Exception e) {
            RawOres.logError("", e);
        }
    }
    
    private static String getOre(ItemStack stack) {
        for (int id : OreDictionary.getOreIDs(stack)) {
            String ore = OreDictionary.getOreName(id);
            if (ore.contains("ore")) return ore;
        }
        return null;
    }
    
}
