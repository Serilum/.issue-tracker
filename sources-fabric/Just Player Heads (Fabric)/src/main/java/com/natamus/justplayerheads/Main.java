/*
 * This is the latest source code of Just Player Heads.
 * Minecraft version: 1.19.2, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.justplayerheads;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.justplayerheads.cmds.CommandJph;
import com.natamus.justplayerheads.config.ConfigHandler;
import com.natamus.justplayerheads.events.PlayerEvent;
import com.natamus.justplayerheads.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			CommandJph.register(dispatcher);
		});
		
		CollectivePlayerEvents.PLAYER_DEATH.register((ServerLevel world, ServerPlayer player) -> {
			PlayerEvent.onPlayerDeath(world, player);
		});
	}
}
