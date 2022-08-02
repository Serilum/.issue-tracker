/*
 * This is the latest source code of Milk All The Mobs.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.milkallthemobs;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.milkallthemobs.events.MilkEvent;
import com.natamus.milkallthemobs.util.Reference;

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
			return MilkEvent.onEntityInteract(player, world, hand, entity, hitResult);
		});
	}
}
