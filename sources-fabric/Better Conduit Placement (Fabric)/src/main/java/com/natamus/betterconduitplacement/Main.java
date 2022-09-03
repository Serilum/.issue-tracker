/*
 * This is the latest source code of Better Conduit Placement.
 * Minecraft version: 1.19.2, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.betterconduitplacement;

import com.natamus.betterconduitplacement.config.ConfigHandler;
import com.natamus.betterconduitplacement.events.ConduitEvent;
import com.natamus.betterconduitplacement.util.Reference;
import com.natamus.collective_fabric.check.RegisterMod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			return ConduitEvent.onWaterClick(player, world, hand);
		});
		
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return ConduitEvent.onConduitClick(player, world, hand, hitResult);
		});
		
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			ConduitEvent.onBlockBreak(world, player, pos, state, entity);
		});
	}
}
