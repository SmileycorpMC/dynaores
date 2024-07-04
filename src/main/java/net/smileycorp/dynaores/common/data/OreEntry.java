package net.smileycorp.dynaores.common.data;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.dynaores.common.Constants;
import net.smileycorp.dynaores.common.block.BlockRawOre;
import net.smileycorp.dynaores.common.item.ItemBlockRawOre;
import net.smileycorp.dynaores.common.item.ItemRawOre;

public abstract class OreEntry {
    
    protected final String name;
    
    protected final ItemRawOre item;
    protected final BlockRawOre block;
    
    protected OreEntry(String name) {
        this.name = name;
        item = new ItemRawOre(this);
        block = new BlockRawOre(this);
        //registering in post init is usually a bad idea, but we have to do all our registering after other mods have loaded
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(item);
        ForgeRegistries.ITEMS.register(new ItemBlockRawOre(this));
        OreDictionary.registerOre("ore" + name, new ItemStack(item));
        OreDictionary.registerOre("raw" + name, new ItemStack(item));
        OreDictionary.registerOre("blockRaw" + name, new ItemStack(block));
        GameRegistry.addShapelessRecipe(Constants.loc("raw" + name), Constants.loc("raw_ore"), new ItemStack(item, 9), Ingredient.fromStacks(new ItemStack(block)));
        GameRegistry.addShapedRecipe(Constants.loc("raw" + name + "block"), Constants.loc("raw_ore_block"), new ItemStack(block),
               "###", "###", "###", '#', Ingredient.fromItem(item));
    }
    
    public String getName() {
        return name;
    }
    
    public ItemRawOre getItem() {
        return item;
    }
    
    public Block getBlock() {
        return block;
    }
    
    public abstract String getLocalizedName();
    
    public abstract int getColour();
    
    public void refresh() {}
    
    public String getOre() {
       return "ore" + name;
    }
    
}
