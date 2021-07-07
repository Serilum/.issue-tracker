/*
 * This is the latest source code of Zombie Horse Spawn.
 * Minecraft version: 1.16.5, mod version: 2.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Zombie Horse Spawn ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.zombiehorsespawn.events;

import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.zombiehorsespawn.config.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ZombieHorseEvent {
	private static CopyOnWriteArrayList<Entity> zombiehorses = new CopyOnWriteArrayList<Entity>();
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof ZombieHorseEntity) {
			if (!zombiehorses.contains(entity)) {
				zombiehorses.add(entity);
			}
		}
	}
	
	int currentticks = 1;
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		World world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (currentticks % 20 != 0) {
			currentticks += 1;
			return;
		}
		currentticks = 1;
		
		if (!ConfigHandler.GENERAL.shouldBurnZombieHorsesInDaylight.get()) {
			return;
		}
		
		if (!world.isDay()) {
			return;
		}
		
		
		for (Entity zombiehorse : zombiehorses) {
			if (zombiehorse.isAlive()) {
				if (!zombiehorse.isInWaterRainOrBubble()) {
					BlockPos epos = zombiehorse.blockPosition();
					if (BlockPosFunctions.isOnSurface(world, epos)) {
						zombiehorse.setSecondsOnFire(3);
					}
				}	
			}
			else {
				zombiehorses.remove(zombiehorse);
			}		
		}
	}
}
