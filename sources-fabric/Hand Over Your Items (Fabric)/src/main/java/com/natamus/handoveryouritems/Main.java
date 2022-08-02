/*
 * This is the latest source code of Hand Over Your Items.
 * Minecraft version: 1.19.1, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.handoveryouritems;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.handoveryouritems.config.ConfigHandler;
import com.natamus.handoveryouritems.events.HandOverEvent;
import com.natamus.handoveryouritems.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return HandOverEvent.onPlayerClick(player, world, hand, entity, hitResult);
		});
	}
}
