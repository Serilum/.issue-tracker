/*
 * This is the latest source code of Dismount Entity.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.dismountentity;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.dismountentity.events.DismountEvent;
import com.natamus.dismountentity.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return DismountEvent.onPlayerInteract(player, world, hand, entity, hitResult);
		});
	}
}
