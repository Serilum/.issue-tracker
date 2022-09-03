/*
 * This is the latest source code of Quick Paths.
 * Minecraft version: 1.19.2, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.quickpaths;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.quickpaths.events.PathEvent;
import com.natamus.quickpaths.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.server.MinecraftServer;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerTickEvents.START_SERVER_TICK.register((MinecraftServer server) -> {
			PathEvent.onServerTick(server);
		});
		
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return PathEvent.onRightClickGrass(player, world, hand, hitResult);
		});
	}
}
