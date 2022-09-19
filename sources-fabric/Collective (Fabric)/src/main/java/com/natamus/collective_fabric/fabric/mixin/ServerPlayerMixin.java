/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.64.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(value = ServerPlayer.class, priority = 1001)
public class ServerPlayerMixin {
	@Inject(method = "tick()V", at = @At(value = "HEAD"))
	public void ServerPlayer_tick(CallbackInfo ci) {
		ServerPlayer player = (ServerPlayer)(Object)this;
		ServerLevel world = (ServerLevel)player.getCommandSenderWorld();
		
		CollectivePlayerEvents.PLAYER_TICK.invoker().onTick(world, player);
	}
	
	@Inject(method = "die(Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At(value = "HEAD"))
	public void ServerPlayer_die(DamageSource damageSource, CallbackInfo ci) {
		ServerPlayer player = (ServerPlayer)(Object)this;
		ServerLevel world = (ServerLevel)player.getCommandSenderWorld();
		
		CollectivePlayerEvents.PLAYER_DEATH.invoker().onDeath(world, player);
	}
	
	@Inject(method = "changeDimension(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/world/entity/Entity;", at = @At(value = "RETURN"))
	public void ServerPlayer_changeDimension(ServerLevel serverLevel, CallbackInfoReturnable<Boolean> ci) {
		ServerPlayer player = (ServerPlayer)(Object)this;
		
		CollectivePlayerEvents.PLAYER_CHANGE_DIMENSION.invoker().onChangeDimension(serverLevel, player);
	}
	
	@Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
	private void ServerPlayer_drop(ItemStack itemStack, boolean bl, boolean bl2, CallbackInfoReturnable<ItemEntity> ci) {
		Player player = (Player)(Object)this;
		CollectiveItemEvents.ON_ITEM_TOSSED.invoker().onItemTossed(player, itemStack);
	}
}
