/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.config.CollectiveConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
import com.natamus.collective_fabric.events.CollectiveEvents;
import com.natamus.collective_fabric.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class CollectiveFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		System.out.println("Loading Collective (Fabric) version " + Reference.VERSION + ".");

		DuskConfig.init(Reference.MOD_ID, CollectiveConfigHandler.class);
		
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			CollectiveEvents.onWorldLoad(world);
		});
		
		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			CollectiveEvents.onWorldTick(world);
		});
		
		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			CollectiveEvents.onEntityJoinLevel(world, entity);
		});

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
}
