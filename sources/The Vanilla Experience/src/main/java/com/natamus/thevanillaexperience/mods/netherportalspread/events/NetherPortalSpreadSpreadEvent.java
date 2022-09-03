/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.netherportalspread.events;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.netherportalspread.config.NetherPortalSpreadConfigHandler;
import com.natamus.thevanillaexperience.mods.netherportalspread.util.NetherPortalSpreadUtil;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.world.BlockEvent.PortalSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class NetherPortalSpreadSpreadEvent {
	private static HashMap<Level, CopyOnWriteArrayList<BlockPos>> portals_to_process = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();
	private static HashMap<Level, Integer> worldticks = new HashMap<Level, Integer>();
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		Level world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.END)) {
			return;
		}
		
		if (WorldFunctions.isNether(world)) {
			return;
		}
		
		if (portals_to_process.get(world).size() > 0) {
			BlockPos portal = portals_to_process.get(world).get(0);
			
			if (!NetherPortalSpreadUtil.portals.get(world).contains(portal) && !NetherPortalSpreadUtil.preventedportals.get(world).containsKey(portal)) {
				NetherPortalSpreadUtil.validatePortalAndAdd(world, portal);
			}
			
			portals_to_process.get(world).remove(0);
		}
		
		int worldtick = worldticks.get(world);
		if (worldtick % NetherPortalSpreadConfigHandler.GENERAL.spreadDelayTicks.get() != 0) {
			worldticks.put(world, worldtick+1);
			return;
		}
		worldticks.put(world, 1);
		
		for (BlockPos portal : NetherPortalSpreadUtil.portals.get(world)) {
        	NetherPortalSpreadUtil.spreadNextBlock(world, portal);
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (WorldFunctions.isNether(world)) {
			return;
		}
		
		worldticks.put(world, 0);
		portals_to_process.put(world, new CopyOnWriteArrayList<BlockPos>());
		NetherPortalSpreadUtil.portals.put(world, new CopyOnWriteArrayList<BlockPos>());
		NetherPortalSpreadUtil.preventedportals.put(world, new HashMap<BlockPos, Boolean>());
		
		NetherPortalSpreadUtil.loadPortalsFromWorld(world);
	}

	@SubscribeEvent
	public void onPortalSpawn(PortalSpawnEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
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
		final Player player = e.getPlayer();

    	Level world = player.getCommandSenderWorld();
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
