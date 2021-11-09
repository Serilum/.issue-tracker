/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.17.x, mod version: 2.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Areas ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.areas.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.areas.config.ConfigHandler;
import com.natamus.areas.objects.AreaObject;
import com.natamus.areas.objects.Variables;
import com.natamus.areas.util.Util;
import com.natamus.collective_fabric.functions.FABFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.collective_fabric.functions.WorldFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

public class AreaEvent {
	int tickdelay = 20;
	
	public static void onWorldLoad(ServerLevel world) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (!Variables.areasperworld.containsKey(world)) {
			Variables.areasperworld.put(world, new HashMap<BlockPos, AreaObject>());
			Variables.ignoresignsperworld.put(world, new CopyOnWriteArrayList<BlockPos>());
			
			Variables.checkifshouldignoreperworld.put(world, new CopyOnWriteArrayList<BlockPos>());
			Variables.ignoremap.put(world, new HashMap<BlockPos, Integer>());
		}
	}
	
	public static void onServerTick(MinecraftServer server) {
		if (!e.phase.equals(Phase.START)) {
			return;
		}
		
		if (tickdelay > 0) {
			tickdelay -= 1;
			return;
		}
		tickdelay = 20;
		
		for (Level world : Variables.checkifshouldignoreperworld.keySet()) {
			for (BlockPos pos : Variables.checkifshouldignoreperworld.get(world)) {
				if (Variables.areasperworld.get(world).containsKey(pos)) {
					Variables.checkifshouldignoreperworld.get(world).remove(pos);
					Variables.ignoremap.get(world).remove(pos);
					continue;
				}
				
				int checkleft = Variables.ignoremap.get(world).get(pos);
				if (checkleft <= 0) {
					Variables.checkifshouldignoreperworld.get(world).remove(pos);
					Variables.ignoremap.get(world).remove(pos);
					
					Variables.ignoresignsperworld.get(world).add(pos);
					continue;
				}
				
				Variables.ignoremap.get(world).put(pos, checkleft-1);
			}
		}
	}
	
	public static void onPlayerTick(ServerLevel world, ServerPlayer player) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.END)) {
			return;
		}
		
		if (player.tickCount % 20 != 0) {
			return;
		}
		
		BlockPos ppos = player.blockPosition();
		
		List<AreaObject> aos = new ArrayList<AreaObject>();
		List<String> enteredareas = new ArrayList<String>();
		List<BlockPos> ignoresigns = Variables.ignoresignsperworld.get(world);
		
		List<BlockPos> nearbysigns = FABFunctions.getAllTileEntityPositionsNearbyEntity(BlockEntityType.SIGN, ConfigHandler.radiusAroundPlayerToCheckForSigns.getValue(), world, player);
		for (BlockPos nspos : nearbysigns) {
			if (ignoresigns.contains(nspos)) {
				continue;
			}
			
			AreaObject ao = Util.getAreaSign(world, nspos);
			if (ao == null) {
				if (!Variables.checkifshouldignoreperworld.get(world).contains(nspos)) {
					Variables.checkifshouldignoreperworld.get(world).add(nspos);
					Variables.ignoremap.get(world).put(nspos, 10);
				}
				continue;
			}
			
			if (ao.containsplayers.contains(player)){
				enteredareas.add(ao.areaname);
			}
			aos.add(ao);
		}
		
		for (AreaObject ao : aos) {
			if (ao == null) {
				continue;
			}
			
			BlockPos nspos = ao.location;
			if (ppos.closerThan(nspos, ao.radius)) {
				if (ao.containsplayers.contains(player)) {
					continue;
				}
				
				if (Collections.frequency(enteredareas, ao.areaname) > 1) {
					Util.enterArea(ao, player, false);
					return;
				}
				
				Util.enterArea(ao, player, true);
			}
			else if (ao.containsplayers.contains(player)){
				if (Collections.frequency(enteredareas, ao.areaname) > 1) {
					Util.exitArea(ao, player, false);
					return;
				}
				
				Util.exitArea(ao, player, true);
			}
		}
	}
	
	public static void onSignBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BlockState state = e.getState();
		if (Util.isSignBlock(state.getBlock())) {
			BlockPos signpos = e.getPos();
			if (Variables.areasperworld.get(world).containsKey(signpos)) {
				AreaObject ao = Variables.areasperworld.get(world).get(signpos);
				List<Player> playersinside = ao.containsplayers;
				for (Player player : playersinside) {
					Util.areaChangeMessage(player, StringFunctions.capitalizeFirst(ao.areaname) + " no longer exists.", ao.customrgb);
				}
				
				Variables.areasperworld.get(world).remove(signpos);
			}
			if (Variables.ignoresignsperworld.get(world).contains(signpos)) {
				Variables.ignoresignsperworld.get(world).remove(signpos);
			}
			
			if (Variables.checkifshouldignoreperworld.get(world).contains(signpos)) {
				Variables.checkifshouldignoreperworld.get(world).remove(signpos);
			}
			if (Variables.ignoremap.get(world).containsKey(signpos)) {
				Variables.ignoremap.get(world).remove(signpos);
			}
		}
	}
}
