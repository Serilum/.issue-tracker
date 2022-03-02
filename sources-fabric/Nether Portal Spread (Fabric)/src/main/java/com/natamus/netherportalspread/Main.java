/*
 * This is the latest source code of Nether Portal Spread.
 * Minecraft version: 1.18.x, mod version: 5.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Nether Portal Spread ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
