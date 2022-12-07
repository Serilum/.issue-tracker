/*
 * This is the latest source code of Random Village Names.
 * Minecraft version: 1.19.3, mod version: 2.2.
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

package com.natamus.randomvillagenames;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.randomvillagenames.events.SetVillageSignEvent;
import com.natamus.randomvillagenames.util.Reference;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel level) -> {
			SetVillageSignEvent.onWorldLoad(level);
		});
		ServerTickEvents.START_WORLD_TICK.register((ServerLevel level) -> {
			SetVillageSignEvent.onWorldTick(level);
		});
		ServerChunkEvents.CHUNK_LOAD.register((ServerLevel level, LevelChunk chunk) -> {
			SetVillageSignEvent.onChunkLoad(level, chunk);
		});
	}
}
