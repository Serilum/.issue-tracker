/*
 * This is the latest source code of Mineral Chance.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.mineralchance;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.mineralchance.config.ConfigHandler;
import com.natamus.mineralchance.events.MiningEvent;
import com.natamus.mineralchance.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			MiningEvent.onBlockBreak(world, player, pos, state, entity);
		});
	}
}
