/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.skeletonhorsespawn.events;

import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.thevanillaexperience.mods.skeletonhorsespawn.config.SkeletonHorseSpawnConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SkeletonHorseSpawnSkeletonHorseEvent {
	private static CopyOnWriteArrayList<Entity> skeletonhorses = new CopyOnWriteArrayList<Entity>();
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof SkeletonHorseEntity) {
			if (!skeletonhorses.contains(entity)) {
				skeletonhorses.add(entity);
			}
		}
	}
	
	int currentticks = 1;
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		World world = e.world;
		if (world.isRemote || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (currentticks % 20 != 0) {
			currentticks += 1;
			return;
		}
		currentticks = 1;
		
		if (!SkeletonHorseSpawnConfigHandler.GENERAL.shouldBurnSkeletonHorsesInDaylight.get()) {
			return;
		}
		
		if (!world.isDaytime()) {
			return;
		}
		
		
		for (Entity skeletonhorse : skeletonhorses) {
			if (skeletonhorse.isAlive()) {
				if (!skeletonhorse.isInWaterRainOrBubbleColumn()) {
					BlockPos epos = skeletonhorse.getPosition();
					if (BlockPosFunctions.isOnSurface(world, epos)) {
						skeletonhorse.setFire(3);
					}
				}	
			}
			else {
				skeletonhorses.remove(skeletonhorse);
			}		
		}
	}
}