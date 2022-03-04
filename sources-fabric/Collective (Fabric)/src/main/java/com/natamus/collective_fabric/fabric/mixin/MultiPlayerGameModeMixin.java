/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.x, mod version: 4.13.
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

import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MultiPlayerGameMode.class, priority = 1001)
public class MultiPlayerGameModeMixin {
	@Shadow @Final private Minecraft minecraft;

	@Inject(method = "useItemOn", at = @At(value = "HEAD"), cancellable = true)
	public void MultiPlayerGameMode_useItemOn(LocalPlayer player, ClientLevel level, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> ci) {
		if (!CollectiveBlockEvents.BLOCK_RIGHT_CLICK.invoker().onBlockRightClick(level, player, interactionHand, blockHitResult.getBlockPos(), blockHitResult)) {
			ci.setReturnValue(InteractionResult.FAIL);
		}
	}

	@Inject(method = "startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyBlock(Lnet/minecraft/core/BlockPos;)Z", ordinal = 0), cancellable = true)
	public void MultiPlayerGameMode_startDestroyBlock_creative(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (!CollectiveBlockEvents.BLOCK_LEFT_CLICK.invoker().onBlockLeftClick(minecraft.level, minecraft.player, blockPos, direction)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"), cancellable = true)
	public void MultiPlayerGameMode_startDestroyBlock_survival(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (!CollectiveBlockEvents.BLOCK_LEFT_CLICK.invoker().onBlockLeftClick(minecraft.level, minecraft.player, blockPos, direction)) {
			cir.setReturnValue(false);
		}
	}

	/*@Inject(method = "useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"), locals = LocalCapture.PRINT)
	public void MultiPlayerGameMode_useItem() {

	}*/
}
