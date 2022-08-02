/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.respawningshulkers.events;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.respawningshulkers.config.RespawningShulkersConfigHandler;
import com.natamus.thevanillaexperience.mods.respawningshulkers.util.Reference;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fmlserverevents.FMLServerStoppingEvent;

@EventBusSubscriber
public class RespawningShulkersShulkerEvent {
	private static HashMap<Entity, Integer> shulkersTicksLeft = new HashMap<Entity, Integer>();
	private static HashMap<Level, CopyOnWriteArrayList<Entity>> respawnShulkers = new HashMap<Level, CopyOnWriteArrayList<Entity>>();
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		respawnShulkers.put(world, new CopyOnWriteArrayList<Entity>());
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		Level world = e.world;
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
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		if (entity instanceof Shulker == false) {
			return;
		}
		
		if (e.isCanceled()) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(Reference.MOD_ID + ".fromspawner")) {
			if (RespawningShulkersConfigHandler.GENERAL.shulkersFromSpawnersDoNotRespawn.get()) {
				return;
			}
		}
		
		Shulker newshulker = EntityType.SHULKER.create(world);
		newshulker.restoreFrom(entity);
		newshulker.setHealth(30F);
		
		shulkersTicksLeft.put(newshulker, RespawningShulkersConfigHandler.GENERAL.timeInTicksToRespawn.get());
		respawnShulkers.get(world).add(newshulker);
	}
	
	@SubscribeEvent
	public void onMobCheckSpawn(LivingSpawnEvent.CheckSpawn e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Shulker == false) {
			return;
		}
		
		if (e.getSpawner() != null) {
			entity.addTag(Reference.MOD_ID + ".fromspawner");
		}
	}
	
	@SubscribeEvent
	public void onServerShutdown(FMLServerStoppingEvent e) {
		Set<Level> worlds = respawnShulkers.keySet();
		for (Level world : worlds) {
			for (Entity shulker : respawnShulkers.get(world)) {
				world.addFreshEntity(shulker);
			}
		}
	}
}
