package net.smileycorp.dynaores.integration;

import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.common.MekanismFluids;
import mekanism.common.recipe.RecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.dynaores.common.Constants;
import net.smileycorp.dynaores.common.item.ItemRawOre;

import java.util.Locale;

public class MekanismIntegration {
    
    public static void registerRecipes(ItemRawOre item, String name) {
        String formattedName = name.replaceAll("(.)([A-Z])", "$1_$2").toLowerCase(Locale.US);
        if (OreDictionary.doesOreNameExist("dust" + name)) {
            ItemStack output = Constants.getStack("dust" + name, 2);
            if (!output.isEmpty()) RecipeHandler.addEnrichmentChamberRecipe(new ItemStack(item), output);
        }
        if (OreDictionary.doesOreNameExist("clump" + name)) {
            ItemStack output = Constants.getStack("clump" + name, 3);
            if (!output.isEmpty()) RecipeHandler.addPurificationChamberRecipe(new ItemStack(item), output);
        }
        if (OreDictionary.doesOreNameExist("shard" + name)) {
            ItemStack output = Constants.getStack("shard" + name, 4);
            if (!output.isEmpty()) RecipeHandler.addChemicalInjectionChamberRecipe(new ItemStack(item), MekanismFluids.HydrogenChloride, output);
        }
        if (GasRegistry.containsGas(formattedName)) RecipeHandler.addChemicalDissolutionChamberRecipe(new ItemStack(item), new GasStack(GasRegistry.getGas(formattedName), 1000));
    }
    
}
