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

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(value = MultiPlayerGameMode.class, priority = 1001)
public class MultiPlayerGameModeMixin {
	@Inject(method = "useItemOn", at = @At(value = "HEAD"), cancellable = true)
	public void MultiPlayerGameMode_useItemOn(LocalPlayer player, ClientLevel level, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> ci) {
		if (!CollectiveBlockEvents.BLOCK_RIGHT_CLICK.invoker().onBlockRightClick(level, player, interactionHand, blockHitResult.getBlockPos(), blockHitResult)) {
			ci.setReturnValue(InteractionResult.FAIL);
		}
	}
}
