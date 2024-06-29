package net.smileycorp.rawores.common.data;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.smileycorp.rawores.common.Constants;
import net.smileycorp.rawores.common.RawOres;

public class ItemRawOre extends Item {
    
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
        return I18n.translateToLocalFormatted("items.raw_ores.RawOre.name", entry.getOres().get(0).getDisplayName().replace("Ore", "").trim()).trim();
    }
    
    public OreEntry getEntry() {
        return entry;
    }
    
}
