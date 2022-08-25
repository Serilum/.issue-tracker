/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.49.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.fabric.mixin;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;
import net.minecraft.client.Minecraft;
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
	public void MultiPlayerGameMode_useItemOn(LocalPlayer localPlayer, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
		if (!CollectiveBlockEvents.BLOCK_RIGHT_CLICK.invoker().onBlockRightClick(localPlayer.level, localPlayer, interactionHand, blockHitResult.getBlockPos(), blockHitResult)) {
			cir.setReturnValue(InteractionResult.FAIL);
		}
	}

	@Inject(method = "startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startPrediction(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/multiplayer/prediction/PredictiveAction;)V", ordinal = 0), cancellable = true)
	public void MultiPlayerGameMode_startDestroyBlock_creative(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (!CollectiveBlockEvents.BLOCK_LEFT_CLICK.invoker().onBlockLeftClick(minecraft.level, minecraft.player, blockPos, direction)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "startDestroyBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startPrediction(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/multiplayer/prediction/PredictiveAction;)V", ordinal = 1), cancellable = true)
	public void MultiPlayerGameMode_startDestroyBlock_survival(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (!CollectiveBlockEvents.BLOCK_LEFT_CLICK.invoker().onBlockLeftClick(minecraft.level, minecraft.player, blockPos, direction)) {
			cir.setReturnValue(false);
		}
	}

	/*@Inject(method = "useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"), locals = LocalCapture.PRINT)
	public void MultiPlayerGameMode_useItem() {

	}*/
}
