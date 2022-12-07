/*
 * This is the latest source code of Replanting Crops.
 * Minecraft version: 1.19.3, mod version: 4.3.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.replantingcrops;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.replantingcrops.config.ConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
import com.natamus.replantingcrops.events.CropEvent;
import com.natamus.replantingcrops.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, entity) -> {
			return CropEvent.onHarvest(world, player, pos, state, entity);
		});
		
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			CropEvent.onHarvest(world, entity);
		});
	}
}
