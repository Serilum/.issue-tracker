/*
 * This is the latest source code of Extract Poison.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.extractpoison;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.extractpoison.config.ConfigHandler;
import com.natamus.extractpoison.events.EntityEvent;
import com.natamus.extractpoison.util.Reference;

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
			return EntityEvent.onEntityInteract(player, world, hand, entity, hitResult);
		});
	}
}
