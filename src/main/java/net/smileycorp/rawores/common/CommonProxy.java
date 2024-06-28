package net.smileycorp.rawores.common;

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

import java.util.List;
import java.util.Random;

public class CommonProxy {
    
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    public void init(FMLInitializationEvent event) {}
    
    public void postInit(FMLPostInitializationEvent event) {
        //we do everything in post init so we can check everything other mods have registered
        OreHandler.INSTANCE.buildOreProperties();
    }
    
    public void serverStart(FMLServerStartingEvent event) {}
    
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
                drops.set(i, new ItemStack(entry.getItem(), getFortune(event.getFortuneLevel(), event.getHarvester().getRNG())));
            }
        } catch (Exception e) {}
    }
    
    private String getOre(ItemStack stack) {
        for (int id : OreDictionary.getOreIDs(stack)) {
            String ore = OreDictionary.getOreName(id);
            if (ore.contains("ore")) return ore;
        }
        return null;
    }
    
    private int getFortune(int fortune, Random rand) {
        return getFortune(fortune, 1, rand);
    }
    
    private int getFortune(int fortune, int base, Random rand) {
        int drops = (Math.max(0, rand.nextInt(fortune + 2) - 1) + 1 + fortune) * base;
        return drops;
    }
    
}
