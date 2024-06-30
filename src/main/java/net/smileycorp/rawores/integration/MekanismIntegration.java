package net.smileycorp.rawores.integration;

import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.common.MekanismFluids;
import mekanism.common.recipe.RecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.rawores.common.Constants;
import net.smileycorp.rawores.common.item.ItemRawOre;

import java.util.Locale;

public class MekanismIntegration {
    
    public static void registerRecipes(ItemRawOre item, String name) {
        String formattedName = name.replaceAll("(.)([A-Z])", "$1_$2").toLowerCase(Locale.US);
        if (OreDictionary.doesOreNameExist("dust" + name)) RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(item), Constants.getStack("dust" + name, 2));
        if (OreDictionary.doesOreNameExist("clump" + name)) RecipeHandler.addPurificationChamberRecipe(new ItemStack(item), Constants.getStack("clump" + name, 3));
        if (OreDictionary.doesOreNameExist("shard" + name)) RecipeHandler.addChemicalInjectionChamberRecipe(new ItemStack(item), MekanismFluids.HydrogenChloride,
                Constants.getStack("shard" + name, 3));
        if (GasRegistry.containsGas(formattedName)) RecipeHandler.addChemicalDissolutionChamberRecipe(new ItemStack(item), new GasStack(GasRegistry.getGas(formattedName), 1000));
    }
    
}
