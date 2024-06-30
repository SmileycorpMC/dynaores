package net.smileycorp.rawores.common.data;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.rawores.common.Constants;

import java.util.Locale;

public class OreEntry {
    
    private final String name, unlocalizedName;
    private final ItemStack ingot;
    private final ItemRawOre item;
    private final BlockRawOre block;
    private final int colour;
    
    public OreEntry(String name, String unlocalizedName, ItemStack ingot) {
        this(name, unlocalizedName, ingot, 0);
    }
    
    public OreEntry(String name, int colour) {
        this(name, "material.raw_ores." + name.toLowerCase(Locale.US), ItemStack.EMPTY, colour);
    }
    
    private OreEntry(String name, String unlocalizedName, ItemStack ingot, int colour) {
        this.name = name;
        this.unlocalizedName = unlocalizedName;
        this.ingot = ingot;
        this.colour = colour;
        this.item = new ItemRawOre(this);
        this.block = new BlockRawOre(this);
        //registering in post init is usually a bad idea, but we have to do all our registering after other mods have loaded
        if (!ingot.isEmpty()) GameRegistry.addSmelting(item, ingot, 0.1f);
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(item);
        ForgeRegistries.ITEMS.register(new ItemBlockRawOre(this));
        OreDictionary.registerOre("ore" + name, new ItemStack(item));
        OreDictionary.registerOre("raw" + name, new ItemStack(item));
        OreDictionary.registerOre("blockRaw" + name, new ItemStack(block));
        GameRegistry.addShapelessRecipe(Constants.loc("raw_" + name), Constants.loc("raw_ore"), new ItemStack(item, 9), Ingredient.fromStacks(new ItemStack(block)));
        GameRegistry.addShapedRecipe(Constants.loc("raw_" + name + "_block"), Constants.loc("raw_ore_block"), new ItemStack(block),
               "###", "###", "###", '#', Ingredient.fromItem(item));
    }
    
    public String getName() {
        return name;
    }
    
    public String getUnlocalizedName() {
        return unlocalizedName;
    }
    
    public ItemStack getIngot() {
        return ingot;
    }
    
    public ItemRawOre getItem() {
        return item;
    }
    
    public Block getBlock() {
        return block;
    }
    
    public int getColour() {
        return colour;
    }
    
}
