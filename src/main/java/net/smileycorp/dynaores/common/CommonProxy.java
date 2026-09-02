package net.smileycorp.dynaores.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.dynaores.common.command.*;
import net.smileycorp.dynaores.common.data.OreCacheLoader;
import net.smileycorp.dynaores.common.data.OreEntry;
import net.smileycorp.dynaores.common.data.OreHandler;

import java.util.List;
import java.util.Random;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.syncConfig(event);
        OreCacheLoader.INSTANCE.tryLoadCache();
        MinecraftForge.EVENT_BUS.register(this);
        OreHandler.INSTANCE.registerConfigOres();
        if (!OreCacheLoader.INSTANCE.isActive() && Loader.isModLoaded("crafttweaker"))
            MinecraftForge.EVENT_BUS.register(new CraftTweakerIntegration());
    }

    public void postInit(FMLPostInitializationEvent event) {
        if (!ConfigHandler.addSmelting) return;
        for (OreEntry entry : OreHandler.INSTANCE.getOres()) {
            if (!entry.isCustom()) continue;
            ItemStack material = entry.getMaterial();
            if (!material.isEmpty()) GameRegistry.addSmelting(entry.getItem(), material, 0.5f);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (OreCacheLoader.INSTANCE.isActive()) return;
        //register vanilla ores now so they get added to the system properly
        OreHandler.INSTANCE.tryRegister("oreCoal", new ItemStack(Blocks.COAL_ORE));
        OreHandler.INSTANCE.tryRegister("oreIron", new ItemStack(Blocks.IRON_ORE));
        OreHandler.INSTANCE.tryRegister("oreGold", new ItemStack(Blocks.GOLD_ORE));
        OreHandler.INSTANCE.tryRegister("oreDiamond", new ItemStack(Blocks.DIAMOND_ORE));
        OreHandler.INSTANCE.tryRegister("oreEmerald", new ItemStack(Blocks.EMERALD_ORE));
        OreHandler.INSTANCE.tryRegister("oreLapis", new ItemStack(Blocks.LAPIS_ORE));
        OreHandler.INSTANCE.tryRegister("oreRedstone", new ItemStack(Blocks.REDSTONE_ORE));
        OreHandler.INSTANCE.tryRegister("oreQuartz", new ItemStack(Blocks.QUARTZ_ORE));
    }

    //high priority so hopefully fortune will stack with other modifiers, use this as a backup in case mods override Block#getDrops
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void dropItem(BlockEvent.HarvestDropsEvent event) {
        if (event.isSilkTouching()) return;
        handleDrops(event.getState(), event.getDrops(), event.getFortuneLevel(), DynaOres.RANDOM);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void oreDict(OreDictionary.OreRegisterEvent event) {
        if (OreCacheLoader.INSTANCE.isActive()) return;
        OreHandler.INSTANCE.tryRegister(event.getName(), event.getOre());
    }

    public static void handleDrops(IBlockState state, List<ItemStack> drops, int fortune, Random rand) {
        //use try/catch here because blocks without items can't be oredicted and the game will crash if you try to make one
        if (Item.getItemFromBlock(state.getBlock()) == Items.AIR) return;
        try {
            //if the block broken isn't an ore return
            //if (getOre(new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state))) == null) return;
            for (int i = 0; i < drops.size(); i++) {
                ItemStack stack = drops.get(i);
                Tuple<OreEntry, Double> pair = getOre(stack);
                if (pair == null) continue;
                drops.set(i, new ItemStack(pair.getFirst().getItem(), getFortune(fortune, pair.getSecond() * stack.getCount(), rand)));
            }
        } catch (Exception e) {}
    }

    //get a pair of an ore entry that matches the pattern
    private static Tuple<OreEntry, Double> getOre(ItemStack stack) {
        for (int id : OreDictionary.getOreIDs(stack)) {
            String ore = OreDictionary.getOreName(id);
            OreEntry entry = OreHandler.INSTANCE.getEntry(ore);
            if (entry != null) return new Tuple<>(entry, ConfigHandler.getMultiplier(ore));
        }
        return null;
    }

    //ore drop formula copied from diamond ore, it's the same formula used by the ore_drops loot function in modern versions
    private static int getFortune(int fortune, double base, Random rand) {
        if (!ConfigHandler.canFortune) return (int) base;
        return (int) ((Math.max(0, rand.nextInt(fortune + 2) - 1) + 1) * base);
    }

    public void serverStarting(FMLServerStartingEvent event) {
        DynaOresCommand command = new DynaOresCommand();
        command.register("getColour", new SubCommandGetColour());
        command.register("getLang", new SubCommandGetLang());
        command.register("getTextureLoc", new SubCommandGetTextureLoc());
        if (!event.getServer().isDedicatedServer()) {
            command.register("clearCache", new SubCommandClearCache());
            command.register("cacheOres", new SubCommandCacheOres());
        }
        event.registerServerCommand(command);
    }

}
