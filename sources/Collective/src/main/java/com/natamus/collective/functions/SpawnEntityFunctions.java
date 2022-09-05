/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.51.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.collective.functions;

import com.natamus.collective.events.CollectiveEvents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class SpawnEntityFunctions {
	public static void spawnEntityOnNextTick(ServerLevel serverworld, Entity entity) {
		CollectiveEvents.entitiesToSpawn.get(serverworld).add(entity);
	}
	
	public static void startRidingEntityOnNextTick(ServerLevel serverworld, Entity ridden, Entity rider) {
		CollectiveEvents.entitiesToRide.get(serverworld).put(ridden, rider);
	}
}
