package net.smileycorp.rawores.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.smileycorp.rawores.common.Constants;
import net.smileycorp.rawores.common.RawOres;
import net.smileycorp.rawores.common.data.OreEntry;

public class ItemRawOre extends Item implements IOreItem {
    
    private final OreEntry entry;
    
    public ItemRawOre(OreEntry entry) {
        this.entry = entry;
        String name = "Raw_" + entry.getName();
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(RawOres.CREATIVE_TAB);
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocalFormatted("items.raw_ores.RawOre.name",
                I18n.translateToLocal(entry.getLocalizedName()).replace("Ore", "").trim()).trim();
    }
    
    @Override
    public OreEntry getEntry() {
        return entry;
    }
    
}
