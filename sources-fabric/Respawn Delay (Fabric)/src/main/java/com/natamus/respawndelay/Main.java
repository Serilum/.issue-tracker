/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.19.1, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.respawndelay;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.respawndelay.cmds.CommandRespawnall;
import com.natamus.respawndelay.config.ConfigHandler;
import com.natamus.respawndelay.events.RespawningEvent;
import com.natamus.respawndelay.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CommandRespawnall.register(dispatcher);
		});
		
		CollectivePlayerEvents.PLAYER_TICK.register((ServerLevel world, ServerPlayer player) -> {
			RespawningEvent.onPlayerTick(world, player);
		});
		
		ServerPlayerEvents.ALLOW_DEATH.register((ServerPlayer player, DamageSource damageSource, float damageAmount) -> {
			return RespawningEvent.onPlayerDeath(player, damageSource, damageAmount);
		});
		
		CollectivePlayerEvents.PLAYER_LOGGED_IN.register((Level world, Player player) -> {
			RespawningEvent.onPlayerLogin(world, player);
		});

		CollectivePlayerEvents.PLAYER_LOGGED_OUT.register((Level world, Player player) -> {
			RespawningEvent.onPlayerLogout(world, player);
		});
	}
}
