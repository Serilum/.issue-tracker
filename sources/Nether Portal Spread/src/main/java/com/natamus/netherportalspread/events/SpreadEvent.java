/*
 * This is the latest source code of Nether Portal Spread.
 * Minecraft version: 1.16.5, mod version: 5.2.
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

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.netherportalspread.config.ConfigHandler;
import com.natamus.netherportalspread.util.Util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.world.BlockEvent.PortalSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SpreadEvent {
	private static HashMap<World, CopyOnWriteArrayList<BlockPos>> portals_to_process = new HashMap<World, CopyOnWriteArrayList<BlockPos>>();
	private static HashMap<World, Integer> worldticks = new HashMap<World, Integer>();
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		World world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.END)) {
			return;
		}
		
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
		if (worldtick % ConfigHandler.GENERAL.spreadDelayTicks.get() != 0) {
			worldticks.put(world, worldtick+1);
			return;
		}
		worldticks.put(world, 1);
		
		for (BlockPos portal : Util.portals.get(world)) {
        	Util.spreadNextBlock(world, portal);
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (WorldFunctions.isNether(world)) {
			return;
		}
		
		worldticks.put(world, 0);
		portals_to_process.put(world, new CopyOnWriteArrayList<BlockPos>());
		Util.portals.put(world, new CopyOnWriteArrayList<BlockPos>());
		Util.preventedportals.put(world, new HashMap<BlockPos, Boolean>());
		
		Util.loadPortalsFromWorld(world);
	}

	@SubscribeEvent
	public void onPortalSpawn(PortalSpawnEvent e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}

		if (WorldFunctions.isNether(world)) {
			return;
		}
		
		BlockPos portalpos = e.getPos();
		portals_to_process.get(world).add(portalpos);
	}
	
	@SubscribeEvent
	public void onDimensionChange(PlayerChangedDimensionEvent e) {
		final PlayerEntity player = e.getPlayer();

    	World world = player.getCommandSenderWorld();
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
