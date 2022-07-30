/*
 * This is the latest source code of Skeleton Horse Spawn.
 * Minecraft version: 1.19.1, mod version: 2.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Skeleton Horse Spawn ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.skeletonhorsespawn.events;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.skeletonhorsespawn.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.level.Level;

public class SkeletonHorseEvent {
	private static HashMap<Level, CopyOnWriteArrayList<Entity>> skeletonhorses_per_world = new HashMap<Level, CopyOnWriteArrayList<Entity>>();
	private static HashMap<Level, Integer> tickdelay_per_world = new HashMap<Level, Integer>();
	
	public static void onWorldLoad(ServerLevel world) {
		skeletonhorses_per_world.put(world, new CopyOnWriteArrayList<Entity>());
		tickdelay_per_world.put(world, 1);
	}
	
	public static void onEntityJoin(Level world, Entity entity) {
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof SkeletonHorse) {
			if (!skeletonhorses_per_world.get(world).contains(entity)) {
				skeletonhorses_per_world.get(world).add(entity);
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
		
		if (!ConfigHandler.shouldBurnSkeletonHorsesInDaylight.getValue()) {
			return;
		}
		
		if (!world.isDay()) {
			return;
		}
		
		for (Entity skeletonhorse : skeletonhorses_per_world.get(world)) {
			if (skeletonhorse.isAlive()) {
				if (!skeletonhorse.isInWaterRainOrBubble()) {
					BlockPos epos = skeletonhorse.blockPosition();
					if (BlockPosFunctions.isOnSurface(world, epos)) {
						skeletonhorse.setSecondsOnFire(3);
					}
				}	
			}
			else {
				skeletonhorses_per_world.get(world).remove(skeletonhorse);
			}		
		}
	}
}