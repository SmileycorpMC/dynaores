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
    @SuppressWarnings("deprecation")
    public String getItemStackDisplayName(ItemStack stack) {
        // If a lang key exists for this Raw Ore Block, use it.
        if (I18n.canTranslate(stack.getUnlocalizedName() + ".name")) {
            return super.getItemStackDisplayName(stack);
        }
        else { // If not, generate a name that makes sense (only for English tho. In other languages it turns into "Block of Raw Iron Ingot", etc.)
            return I18n.translateToLocalFormatted("items.dynaores.RawOreBlock.name",
                    I18n.translateToLocal(entry.getLocalizedName()).replace("Ingot", "").trim()).trim();
        }
    }
    
    @Override
    public OreEntry getEntry() {
        return entry;
    }
    
}
