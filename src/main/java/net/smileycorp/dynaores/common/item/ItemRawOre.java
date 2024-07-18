package net.smileycorp.dynaores.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.smileycorp.dynaores.common.Constants;
import net.smileycorp.dynaores.common.DynaOres;
import net.smileycorp.dynaores.common.data.OreEntry;

public class ItemRawOre extends Item implements IOreItem {
    
    private final OreEntry entry;
    
    public ItemRawOre(OreEntry entry) {
        this.entry = entry;
        String name = "Raw" + entry.getName();
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(DynaOres.CREATIVE_TAB);
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public String getItemStackDisplayName(ItemStack stack) {
        // If a lang key exists for this Raw Ore, behave as default.
        if (I18n.canTranslate(stack.getUnlocalizedName() + ".name")) {
            return super.getItemStackDisplayName(stack);
        }
        else { // If not, generate a name that makes sense (only for English tho. In other languages it turns into "Raw Iron Ingot", etc.)
            return I18n.translateToLocalFormatted("items.dynaores.RawOre.name",
                    I18n.translateToLocal(entry.getLocalizedName()).replace("Ingot", "").trim()).trim();
        }
        
    }
    
    @Override
    public OreEntry getEntry() {
        return entry;
    }
    
}
