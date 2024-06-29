package net.smileycorp.rawores.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.rawores.common.data.OreEntry;
import net.smileycorp.rawores.common.data.OreHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Block.class)
public class MixinBlock {
    
    private Random rand = new Random();
    
    @Inject(at = @At("RETURN"), method = "getDrops(Lnet/minecraft/util/NonNullList;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)V", remap = false)
    public void rawores$getDrops(NonNullList<ItemStack> drops, IBlockAccess access, BlockPos pos, IBlockState state, int fortune, CallbackInfo callback) {
        try {
            ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().damageDropped(state));
            //get the first oredictionary that matches our pattern
            String ore = getOre(stack);
            if (ore == null) return;
            //get the registered ore entry corresponding to the block we just broke
            OreEntry entry = OreHandler.INSTANCE.getEntry(ore.replace("ore", ""));
            if (entry == null) return;
            for (int i = 0; i < drops.size(); i++) {
                ItemStack drop = drops.get(i);
                //check the drop is the same as the block we just broke so we don't modify extra or changed drops if other mods added them first
                if (!ItemStack.areItemsEqual(stack, drop)) continue;
                drops.set(i, new ItemStack(entry.getItem(), getFortune(fortune, rand)));
            }
        } catch (Exception e) {}
    }
    
    
    private String getOre(ItemStack stack) {
        for (int id : OreDictionary.getOreIDs(stack)) {
            String ore = OreDictionary.getOreName(id);
            if (ore.contains("ore")) return ore;
        }
        return null;
    }
    
    //default to 1, might add a config to change this per ore in future
    private int getFortune(int fortune, Random rand) {
        return getFortune(fortune, 1, rand);
    }
    
    //ore drop formula copied from diamond ore, it's the same formula used by the ore_drops loot function in modern versions
    private int getFortune(int fortune, int base, Random rand) {
        return (Math.max(0, rand.nextInt(fortune + 2) - 1) + 1) * base;
    }
    
    
}
