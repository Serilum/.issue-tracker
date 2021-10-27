/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.61.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.fabric.mixin.crop;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveCropEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = NetherWartBlock.class, priority = 1001)
public class NetherWartBlockMixin {
	@Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Random;nextInt(I)I"), cancellable = true)
	public void NetherWartBlock_randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random, CallbackInfo ci) {
		if (!CollectiveCropEvents.PRE_CROP_GROW.invoker().onPreCropGrow(serverLevel, blockPos, blockState)) {
			ci.cancel();
		}
	}
}
