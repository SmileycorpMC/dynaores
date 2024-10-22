package net.smileycorp.dynaores.common.item;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.smileycorp.dynaores.common.data.OreEntry;

public class ItemBlockRawOre extends ItemBlock implements IOreItem {
    
    private final OreEntry entry;
    
    public ItemBlockRawOre(OreEntry entry) {
        super(entry.getBlock());
        this.entry = entry;
        setUnlocalizedName(getBlock().getUnlocalizedName());
        setRegistryName(getBlock().getRegistryName());
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return I18n.translateToLocalFormatted("items.dynaores.RawOreBlock.name",
                I18n.translateToLocal(entry.getLocalizedName()).replace("Ingot", "").trim()).trim();
    }
    
    @Override
    public OreEntry getEntry() {
        return entry;
    }
    
}
