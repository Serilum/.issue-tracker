/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.36.
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
import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;
import com.natamus.collective_fabric.functions.TaskFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerGameMode.class, priority = 1001)
public class ServerPlayerGameModeMixin {
	@Shadow protected ServerLevel level;
	@Shadow @Final protected ServerPlayer player;
	@Shadow private BlockPos destroyPos;

	@Inject(method = "useItemOn", at = @At(value = "HEAD"), cancellable = true)
	public void ServerPlayerGameMode_useItemOn(ServerPlayer serverPlayer, Level level, ItemStack itemStack, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> ci) {
		if (!CollectiveBlockEvents.BLOCK_RIGHT_CLICK.invoker().onBlockRightClick(level, serverPlayer, interactionHand, blockHitResult.getBlockPos(), blockHitResult)) {
			ci.setReturnValue(InteractionResult.FAIL);
		}
	}

	@Inject(method = "handleBlockBreakAction(Lnet/minecraft/core/BlockPos;Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;Lnet/minecraft/core/Direction;II)V", at = @At(value = "HEAD"), cancellable = true)
	public void ServerPlayerGameMode_handleBlockBreakAction(BlockPos blockPos, ServerboundPlayerActionPacket.Action action, Direction direction, int i, int j, CallbackInfo ci) {
		if (!CollectiveBlockEvents.BLOCK_LEFT_CLICK.invoker().onBlockLeftClick(level, player, blockPos, direction)) {
			player.connection.send(new ClientboundBlockUpdatePacket(blockPos, level.getBlockState(blockPos)));
			level.sendBlockUpdated(blockPos, level.getBlockState(blockPos), level.getBlockState(blockPos), 3);
			ci.cancel();
		}
	}

	@Inject(method = "handleBlockBreakAction(Lnet/minecraft/core/BlockPos;Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;Lnet/minecraft/core/Direction;II)V", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Objects;equals(Ljava/lang/Object;Ljava/lang/Object;)Z", ordinal = 0), cancellable = true)
	public void ServerPlayerGameMode_silence_block_mismatch(BlockPos blockPos, ServerboundPlayerActionPacket.Action action, Direction direction, int i, int j, CallbackInfo ci) {
		this.level.destroyBlockProgress(this.player.getId(), this.destroyPos, -1);
		this.player.connection.send(new ClientboundBlockUpdatePacket(this.destroyPos, this.level.getBlockState(this.destroyPos)));
		this.level.destroyBlockProgress(this.player.getId(), blockPos, -1);
		this.player.connection.send(new ClientboundBlockUpdatePacket(blockPos, this.level.getBlockState(blockPos)));
		ci.cancel();
	}

	@Inject(method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;mineBlock(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)V"))
	public void destroyBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemStack = player.getMainHandItem();
		if ((itemStack.getMaxDamage() - itemStack.getDamageValue()) < 3) {
			ItemStack copy = itemStack.copy();
			TaskFunctions.enqueueTask(player.level, () -> {
				if (itemStack.isEmpty() && !copy.isEmpty()) {
					CollectiveItemEvents.ON_ITEM_DESTROYED.invoker().onItemDestroyed(player, copy, InteractionHand.MAIN_HAND);
				}
			}, 0);
		}
	}
}
