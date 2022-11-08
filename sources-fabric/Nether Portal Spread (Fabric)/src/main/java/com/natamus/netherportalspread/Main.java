/*
 * This is the latest source code of Nether Portal Spread.
 * Minecraft version: 1.19.2, mod version: 6.5.
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

package com.natamus.netherportalspread;

import java.io.IOException;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;
import com.natamus.collective_fabric.fabric.callbacks.CollectivePlayerEvents;
import com.natamus.netherportalspread.config.ConfigHandler;
import com.natamus.collective_fabric.config.DuskConfig;
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
		DuskConfig.init(Reference.MOD_ID, ConfigHandler.class);

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
    	try {
			Util.loadSpreadBlocks();
		} catch (IOException e) {
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
