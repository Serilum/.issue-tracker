/*
 * This is the latest source code of Nether Portal Spread.
 * Minecraft version: 1.19.2, mod version: 6.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.netherportalspread;

import java.io.IOException;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.netherportalspread.config.ConfigHandler;
import com.natamus.netherportalspread.events.SpreadEvent;
import com.natamus.netherportalspread.util.Reference;
import com.natamus.netherportalspread.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalShape;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
    	try {
			Util.loadSpreadBlocks();
		} catch (IOException e) {
			System.out.println(e);
		}
		
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel world) -> {
			SpreadEvent.onWorldLoad(world);
		});
		
		ServerTickEvents.START_WORLD_TICK.register((ServerLevel world) -> {
			SpreadEvent.onWorldTick(world);
		});
		
		CollectiveBlockEvents.ON_NETHER_PORTAL_SPAWN.register((Level world, BlockPos pos, PortalShape shape) -> {
			SpreadEvent.onPortalSpawn(world, pos, shape);
		});
		
		CollectivePlayerEvents.PLAYER_CHANGE_DIMENSION.register((ServerLevel world, ServerPlayer player) -> {
			SpreadEvent.onDimensionChange(world, player);
		}); 
	}
}
