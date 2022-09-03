/*
 * This is the latest source code of Skeleton Horse Spawn.
 * Minecraft version: 1.19.2, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.skeletonhorsespawn;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.objects.SAMObject;
import com.natamus.skeletonhorsespawn.config.ConfigHandler;
import com.natamus.skeletonhorsespawn.events.SkeletonHorseEvent;
import com.natamus.skeletonhorsespawn.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			SkeletonHorseEvent.onWorldLoad(world);
		});
		
		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			SkeletonHorseEvent.onWorldTick(world);
		});
		
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			SkeletonHorseEvent.onEntityJoin(world, entity);
		});
		
    	new SAMObject(EntityType.SKELETON, EntityType.SKELETON_HORSE, null, ConfigHandler.chanceSurfaceSkeletonHasHorse.getValue(), false, true, ConfigHandler.onlySpawnSkeletonHorsesOnSurface.getValue());
	}
}
