package net.smileycorp.dynaores.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.dynaores.common.CommonProxy;
import net.smileycorp.dynaores.common.DynaOres;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlock {
    
    @Inject(at = @At("RETURN"), method = "getDrops(Lnet/minecraft/util/NonNullList;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)V", remap = false)
    public void dynaores$getDrops(NonNullList<ItemStack> drops, IBlockAccess access, BlockPos pos, IBlockState state, int fortune, CallbackInfo callback) {
       CommonProxy.handleDrops(state, drops, fortune, DynaOres.RANDOM);
    }
    
}
