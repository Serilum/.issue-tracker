/*
 * This is the latest source code of Conduits Prevent Drowned.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.conduitspreventdrowned;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveSpawnEvents;
import com.natamus.conduitspreventdrowned.config.ConfigHandler;
import com.natamus.conduitspreventdrowned.events.DrownedEvent;
import com.natamus.conduitspreventdrowned.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveSpawnEvents.MOB_CHECK_SPAWN.register((Mob entity, ServerLevel world, BlockPos spawnerPos, MobSpawnType spawnReason) -> {
			return DrownedEvent.onDrownedSpawn(entity, world, spawnerPos, spawnReason);
		});
	}
}
