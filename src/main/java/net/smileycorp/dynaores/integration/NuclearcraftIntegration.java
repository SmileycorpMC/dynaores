package net.smileycorp.dynaores.integration;

import nc.recipe.AbstractRecipeHandler;
import nc.recipe.NCRecipes;
import nc.util.FluidStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.dynaores.common.Constants;
import net.smileycorp.dynaores.common.item.ItemRawOre;

import java.util.Locale;

public class NuclearcraftIntegration {
    
    public static void registerRecipes(ItemRawOre item, String name) {
        String formattedName = name.replaceAll("(.)([A-Z])", "$1_$2").toLowerCase(Locale.US);
        if (OreDictionary.doesOreNameExist("dust" + name)) {
            ItemStack output = Constants.getStack("dust" + name, 2);
            if (!output.isEmpty()) NCRecipes.manufactory.addRecipe(item, output, 1.25, 1);
        }
        if (FluidRegistry.isFluidRegistered(formattedName)) {
            NCRecipes.melter.addRecipe(item, AbstractRecipeHandler.fluidStack(formattedName, FluidStackHelper.INGOT_ORE_VOLUME), 1.25, 1.5);
        }
    }
    
    
}
