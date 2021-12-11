/*
 * This is the latest source code of Surface Mushrooms.
 * Minecraft version: 1.18.1, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Surface Mushrooms ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.surfacemushrooms.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = MushroomBlock.class, priority = 1001)
public class MushroomBlockMixin {
    @Inject(method = "canSurvive(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z", at = @At(value="HEAD"), cancellable = true)
    private void canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos2 = blockPos.below();
        BlockState blockstate = levelReader.getBlockState(blockPos2);
        if (blockstate.is(BlockTags.MUSHROOM_GROW_BLOCK) || blockstate.canSustainPlant(levelReader, blockPos2, net.minecraft.core.Direction.UP, (MushroomBlock)(Object)this)) {
            cir.setReturnValue(true);
        }
    }
}
