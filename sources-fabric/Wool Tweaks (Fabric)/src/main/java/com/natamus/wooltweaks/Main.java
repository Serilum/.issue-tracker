/*
 * This is the latest source code of Wool Tweaks.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.wooltweaks;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.wooltweaks.events.WoolClickEvent;
import com.natamus.wooltweaks.util.Reference;
import com.natamus.wooltweaks.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
     	Util.initiateColourMaps();
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return WoolClickEvent.onWoolClick(player, world, hand, hitResult);
		});
	}
}
