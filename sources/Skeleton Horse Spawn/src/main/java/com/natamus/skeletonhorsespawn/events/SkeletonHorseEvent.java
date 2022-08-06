/*
 * This is the latest source code of Skeleton Horse Spawn.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.skeletonhorsespawn.events;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.skeletonhorsespawn.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SkeletonHorseEvent {
	private static HashMap<Level, CopyOnWriteArrayList<Entity>> skeletonhorses_per_world = new HashMap<Level, CopyOnWriteArrayList<Entity>>();
	private static HashMap<Level, Integer> tickdelay_per_world = new HashMap<Level, Integer>();
	
	@SubscribeEvent
	public void onWorldLoad(LevelEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		skeletonhorses_per_world.put(world, new CopyOnWriteArrayList<Entity>());
		tickdelay_per_world.put(world, 1);
	}
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof SkeletonHorse) {
			if (!skeletonhorses_per_world.get(world).contains(entity)) {
				skeletonhorses_per_world.get(world).add(entity);
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(LevelTickEvent e) {
		Level world = e.level;
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		int ticks = tickdelay_per_world.get(world);
		if (ticks % 20 != 0) {
			tickdelay_per_world.put(world, ticks+1);
			return;
		}
		tickdelay_per_world.put(world, 1);
		
		if (!ConfigHandler.GENERAL.shouldBurnSkeletonHorsesInDaylight.get()) {
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