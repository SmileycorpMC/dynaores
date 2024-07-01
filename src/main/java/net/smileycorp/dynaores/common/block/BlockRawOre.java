package net.smileycorp.dynaores.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.smileycorp.dynaores.common.Constants;
import net.smileycorp.dynaores.common.DynaOres;
import net.smileycorp.dynaores.common.data.OreEntry;

public class BlockRawOre extends Block {
    
    private final OreEntry entry;
    
    public BlockRawOre(OreEntry entry) {
        super(Material.ROCK);
        this.entry = entry;
        setHardness(6);
        setResistance(6);
        String name = "Raw" + entry.getName() + "Block";
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(DynaOres.CREATIVE_TAB);
    }
    
    public OreEntry getEntry() {
        return entry;
    }
    
}
