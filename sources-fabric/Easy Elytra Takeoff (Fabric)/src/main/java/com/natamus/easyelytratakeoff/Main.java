/*
 * This is the latest source code of Easy Elytra Takeoff.
 * Minecraft version: 1.19.1, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.easyelytratakeoff;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.easyelytratakeoff.events.ElytraEvent;
import com.natamus.easyelytratakeoff.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			return ElytraEvent.onFirework(player, world, hand);
		});
	}
}
