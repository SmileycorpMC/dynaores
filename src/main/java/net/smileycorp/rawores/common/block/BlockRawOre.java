package net.smileycorp.rawores.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.smileycorp.rawores.common.Constants;
import net.smileycorp.rawores.common.RawOres;
import net.smileycorp.rawores.common.data.OreEntry;

public class BlockRawOre extends Block {
    
    private final OreEntry entry;
    
    public BlockRawOre(OreEntry entry) {
        super(Material.ROCK);
        this.entry = entry;
        setHardness(6);
        setResistance(6);
        String name = "Raw_" + entry.getName() + "_Block";
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(RawOres.CREATIVE_TAB);
    }
    
    public OreEntry getEntry() {
        return entry;
    }
    
}
