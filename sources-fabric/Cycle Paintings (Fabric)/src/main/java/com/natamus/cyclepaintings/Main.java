/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.cyclepaintings;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.cyclepaintings.config.ConfigHandler;
import com.natamus.cyclepaintings.events.PaintingEvent;
import com.natamus.cyclepaintings.util.Reference;
import com.natamus.cyclepaintings.util.Util;

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
		Util.setPaintings();
		
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return PaintingEvent.onClick(player, world, hand, entity, hitResult);
		});
	}
}
