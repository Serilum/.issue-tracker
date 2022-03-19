/*
 * This is the latest source code of Nether Portal Spread.
 * Minecraft version: 1.19.x, mod version: 5.8.
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

package com.natamus.netherportalspread.events;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective_fabric.functions.WorldFunctions;
import com.natamus.netherportalspread.config.ConfigHandler;
import com.natamus.netherportalspread.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalShape;

public class SpreadEvent {
	private static HashMap<Level, CopyOnWriteArrayList<BlockPos>> portals_to_process = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();
	private static HashMap<Level, Integer> worldticks = new HashMap<Level, Integer>();
	
	public static void onWorldTick(ServerLevel world) {
		if (WorldFunctions.isNether(world)) {
			return;
		}
		
		if (portals_to_process.get(world).size() > 0) {
			BlockPos portal = portals_to_process.get(world).get(0);
			
			if (!Util.portals.get(world).contains(portal) && !Util.preventedportals.get(world).containsKey(portal)) {
				Util.validatePortalAndAdd(world, portal);
			}
			
			portals_to_process.get(world).remove(0);
		}
		
		int worldtick = worldticks.get(world);
		if (worldtick % ConfigHandler.spreadDelayTicks.getValue() != 0) {
			worldticks.put(world, worldtick+1);
			return;
		}
		worldticks.put(world, 1);
		
		for (BlockPos portal : Util.portals.get(world)) {
        	Util.spreadNextBlock(world, portal);
		}
	}
	
	public static void onWorldLoad(ServerLevel world) {
		if (WorldFunctions.isNether(world)) {
			return;
		}
		
		worldticks.put(world, 0);
		portals_to_process.put(world, new CopyOnWriteArrayList<BlockPos>());
		Util.portals.put(world, new CopyOnWriteArrayList<BlockPos>());
		Util.preventedportals.put(world, new HashMap<BlockPos, Boolean>());
		
		Util.loadPortalsFromWorld(world);
	}

	public static void onPortalSpawn(Level world, BlockPos pos, PortalShape shape) {
		if (world.isClientSide) {
			return;
		}

		if (WorldFunctions.isNether(world)) {
			return;
		}
		
		portals_to_process.get(world).add(pos);
	}
	
	public static void onDimensionChange(ServerLevel world, ServerPlayer player) {
    	if (world.isClientSide) {
    		return;
    	}
    	
		if (WorldFunctions.isNether(world)) {
			return;
		}
    	
    	BlockPos ppos = player.blockPosition();
    	portals_to_process.get(world).add(ppos);
	}
}
