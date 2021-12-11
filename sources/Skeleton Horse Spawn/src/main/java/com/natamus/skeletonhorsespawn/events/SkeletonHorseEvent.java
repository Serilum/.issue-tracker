/*
 * This is the latest source code of Skeleton Horse Spawn.
 * Minecraft version: 1.18.1, mod version: 2.0.
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

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.skeletonhorsespawn.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SkeletonHorseEvent {
	private static HashMap<Level, CopyOnWriteArrayList<Entity>> skeletonhorses_per_world = new HashMap<Level, CopyOnWriteArrayList<Entity>>();
	private static HashMap<Level, Integer> tickdelay_per_world = new HashMap<Level, Integer>();
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		skeletonhorses_per_world.put(world, new CopyOnWriteArrayList<Entity>());
		tickdelay_per_world.put(world, 1);
	}
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
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
	public void onWorldTick(WorldTickEvent e) {
		Level world = e.world;
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