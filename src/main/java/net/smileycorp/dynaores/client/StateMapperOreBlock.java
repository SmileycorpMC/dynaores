package net.smileycorp.dynaores.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class StateMapperOreBlock extends StateMapperBase {
    
    private final ModelResourceLocation loc;
    
    public StateMapperOreBlock(ModelResourceLocation loc) {
        this.loc = loc;
    }
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return loc;
    }
    
}
