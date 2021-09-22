/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.41.
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

@Mixin(ServerPlayer.class)
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
}
