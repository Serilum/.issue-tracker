/*
 * This is the latest source code of Zombie Horse Spawn.
 * Minecraft version: 1.19.1, mod version: 3.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.zombiehorsespawn.events;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.zombiehorsespawn.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.level.Level;

public class ZombieHorseEvent {
	public static HashMap<Level, CopyOnWriteArrayList<Entity>> zombiehorses_per_world = new HashMap<Level, CopyOnWriteArrayList<Entity>>();
	public static HashMap<Level, Integer> tickdelay_per_world = new HashMap<Level, Integer>();
	
	public static void onWorldLoad(ServerLevel world) {
		zombiehorses_per_world.put(world, new CopyOnWriteArrayList<Entity>());
		tickdelay_per_world.put(world, 1);
	}
	
	public static void onEntityJoin(Level world, Entity entity) {
		if (entity instanceof ZombieHorse) {
			if (!zombiehorses_per_world.get(world).contains(entity)) {
				zombiehorses_per_world.get(world).add(entity);
			}
		}
	}
	
	public static void onWorldTick(ServerLevel world) {
		int ticks = tickdelay_per_world.get(world);
		if (ticks % 20 != 0) {
			tickdelay_per_world.put(world, ticks+1);
			return;
		}
		tickdelay_per_world.put(world, 1);
		
		if (!ConfigHandler.shouldBurnZombieHorsesInDaylight.getValue()) {
			return;
		}
		
		if (!world.isDay()) {
			return;
		}
		
		for (Entity zombiehorse : zombiehorses_per_world.get(world)) {
			if (zombiehorse.isAlive()) {
				if (!zombiehorse.isInWaterRainOrBubble()) {
					BlockPos epos = zombiehorse.blockPosition();
					if (BlockPosFunctions.isOnSurface(world, epos)) {
						zombiehorse.setSecondsOnFire(3);
					}
				}	
			}
			else {
				zombiehorses_per_world.get(world).remove(zombiehorse);
			}		
		}
	}
}
