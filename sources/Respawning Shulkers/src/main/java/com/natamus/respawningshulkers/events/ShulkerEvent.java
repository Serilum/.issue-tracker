/*
 * This is the latest source code of Respawning Shulkers.
 * Minecraft version: 1.16.5, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Respawning Shulkers ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.respawningshulkers.events;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.respawningshulkers.config.ConfigHandler;
import com.natamus.respawningshulkers.util.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

@EventBusSubscriber
public class ShulkerEvent {
	private static HashMap<Entity, Integer> shulkersTicksLeft = new HashMap<Entity, Integer>();
	private static HashMap<World, CopyOnWriteArrayList<Entity>> respawnShulkers = new HashMap<World, CopyOnWriteArrayList<Entity>>();
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		respawnShulkers.put(world, new CopyOnWriteArrayList<Entity>());
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		World world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (respawnShulkers.get(world).size() > 0) {
			for (Entity shulker : respawnShulkers.get(world)) {
				int ticksleft = shulkersTicksLeft.get(shulker) - 1;
				if (ticksleft == 0) {
					respawnShulkers.get(world).remove(shulker);
					shulkersTicksLeft.remove(shulker);
					
					world.addFreshEntity(shulker);
					continue;
				}
				
				shulkersTicksLeft.put(shulker, ticksleft);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onShulkerDeath(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		if (entity instanceof ShulkerEntity == false) {
			return;
		}
		
		if (e.isCanceled()) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(Reference.MOD_ID + ".fromspawner")) {
			if (ConfigHandler.GENERAL.shulkersFromSpawnersDoNotRespawn.get()) {
				return;
			}
		}
		
		ShulkerEntity newshulker = EntityType.SHULKER.create(world);
		newshulker.restoreFrom(entity);
		newshulker.setHealth(30F);
		
		shulkersTicksLeft.put(newshulker, ConfigHandler.GENERAL.timeInTicksToRespawn.get());
		respawnShulkers.get(world).add(newshulker);
	}
	
	@SubscribeEvent
	public void onMobCheckSpawn(LivingSpawnEvent.CheckSpawn e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof ShulkerEntity == false) {
			return;
		}
		
		if (e.getSpawner() != null) {
			entity.addTag(Reference.MOD_ID + ".fromspawner");
		}
	}
	
	@SubscribeEvent
	public void onServerShutdown(FMLServerStoppingEvent e) {
		Set<World> worlds = respawnShulkers.keySet();
		for (World world : worlds) {
			for (Entity shulker : respawnShulkers.get(world)) {
				world.addFreshEntity(shulker);
			}
		}
	}
}
