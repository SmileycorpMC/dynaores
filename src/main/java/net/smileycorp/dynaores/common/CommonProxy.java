package net.smileycorp.dynaores.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.dynaores.common.data.OreEntry;
import net.smileycorp.dynaores.common.data.OreHandler;

import java.util.List;
import java.util.Random;

public class CommonProxy {
    
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.syncConfig(event);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public void init(FMLInitializationEvent event) {}
    
    public void postInit(FMLPostInitializationEvent event) {
        //we do everything in post init so we can check everything other mods have registered
        OreHandler.INSTANCE.buildOreProperties();
    }
    
    public void serverStart(FMLServerStartingEvent event) {}
    
    //high priority so hopefully fortune will stack with other modifiers, use this as a backup in case mods override Block#getDrops
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void dropItem(BlockEvent.HarvestDropsEvent event) {
        if (event.isSilkTouching()) return;
        handleDrops(event.getState(),  event.getDrops(), event.getFortuneLevel(), new Random());
    }
    
    public static void handleDrops(IBlockState state, List<ItemStack> drops, int fortune, Random rand) {
        //use try/catch here because blocks without items can't be oredicted and the game will crash if you try to make one
        try {
            ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
            //get the first oredictionary that matches our pattern
            String ore = getOre(stack);
            if (ore == null) return;
            //get the registered ore entry corresponding to the block we just broke
            OreEntry entry = OreHandler.INSTANCE.getEntry(ore);
            if (entry == null) return;
            for (int i = 0; i < drops.size(); i++) {
                ItemStack drop = drops.get(i);
                //check the drop is the same as the block we just broke so we don't modify extra or changed drops if other mods added them first
                if (!ItemStack.areItemsEqual(stack, drop)) continue;
                drops.set(i, new ItemStack(entry.getItem(), getFortune(fortune, rand)));
            }
        } catch (Exception e) {}
    }
    
    private static String getOre(ItemStack stack) {
        for (int id : OreDictionary.getOreIDs(stack)) {
            String ore = OreDictionary.getOreName(id);
            if (ore.contains("ore")) return ore;
        }
        return null;
    }
    
    //default to 1, might add a config to change this per ore in future
    private static int getFortune(int fortune, Random rand) {
        return getFortune(fortune, 1, rand);
    }
    
    //ore drop formula copied from diamond ore, it's the same formula used by the ore_drops loot function in modern versions
    private static int getFortune(int fortune, int base, Random rand) {
        if (!ConfigHandler.canFortune) return base;
        int drops = (Math.max(0, rand.nextInt(fortune + 2) - 1) + 1) * base;
        return drops;
    }
    
}
