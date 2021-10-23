/*
 * This is the latest source code of Respawning Shulkers.
 * Minecraft version: 1.17.x, mod version: 1.6.
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

import com.natamus.collective_fabric.util.Reference;
import com.natamus.respawningshulkers.config.ConfigHandler;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.Level;

public class ShulkerEvent {
	private static HashMap<Entity, Integer> shulkersTicksLeft = new HashMap<Entity, Integer>();
	private static HashMap<Level, CopyOnWriteArrayList<Entity>> respawnShulkers = new HashMap<Level, CopyOnWriteArrayList<Entity>>();
	
	public static void onWorldLoad(ServerLevel world) {
		respawnShulkers.put(world, new CopyOnWriteArrayList<Entity>());
	}
	
	public static void onWorldTick(ServerLevel world) {
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
	
	public static void onShulkerDeath(Level world, Entity entity, DamageSource source) {
		if (world.isClientSide) {
			return;
		}
		if (entity instanceof Shulker == false) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(Reference.MOD_ID + ".fromspawner")) {
			if (ConfigHandler.shulkersFromSpawnersDoNotRespawn.getValue()) {
				return;
			}
		}
		
		Shulker newshulker = EntityType.SHULKER.create(world);
		newshulker.restoreFrom(entity);
		newshulker.setHealth(30F);
		
		shulkersTicksLeft.put(newshulker, ConfigHandler.timeInTicksToRespawn.getValue());
		respawnShulkers.get(world).add(newshulker);
	}
	
	public static void onServerShutdown(MinecraftServer server) {
		Set<Level> worlds = respawnShulkers.keySet();
		for (Level world : worlds) {
			for (Entity shulker : respawnShulkers.get(world)) {
				world.addFreshEntity(shulker);
			}
		}
	}
}
