/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.50.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.fabric.mixin.crop;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveCropEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ChorusFlowerBlock.class, priority = 1001)
public class ChorusFlowerBlockMixin {
	@Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;"), cancellable = true)
	public void ChorusFlowerBlock_randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, CallbackInfo ci) {
		if (!CollectiveCropEvents.PRE_CROP_GROW.invoker().onPreCropGrow(serverLevel, blockPos, blockState)) {
			ci.cancel();
		}
	}
}
