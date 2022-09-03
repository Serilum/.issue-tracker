/*
 * This is the latest source code of Sleep Sooner.
 * Minecraft version: 1.19.2, mod version: 3.2.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.sleepsooner;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.sleepsooner.config.ConfigHandler;
import com.natamus.sleepsooner.events.PlayerEvent;
import com.natamus.sleepsooner.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return PlayerEvent.playerClick(player, world, hand, hitResult);
		});
	}
}
