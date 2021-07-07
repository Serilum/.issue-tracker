/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.16.5, mod version: 2.3.
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
import com.natamus.collective.functions.FABFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AreaEvent {
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (!Variables.areasperworld.containsKey(world)) {
			Variables.areasperworld.put(world, new HashMap<BlockPos, AreaObject>());
			Variables.ignoresignsperworld.put(world, new CopyOnWriteArrayList<BlockPos>());
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		PlayerEntity player = e.player;
		World world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (player.tickCount % 20 != 0) {
			return;
		}
		
		BlockPos ppos = player.blockPosition();
		
		List<AreaObject> aos = new ArrayList<AreaObject>();
		List<String> enteredareas = new ArrayList<String>();
		List<BlockPos> ignoresigns = Variables.ignoresignsperworld.get(world);
		
		List<BlockPos> nearbysigns = FABFunctions.getAllTileEntityPositionsNearbyEntity(TileEntityType.SIGN, ConfigHandler.GENERAL.radiusAroundPlayerToCheckForSigns.get(), world, player);
		for (BlockPos nspos : nearbysigns) {
			if (ignoresigns.contains(nspos)) {
				continue;
			}
			
			AreaObject ao = Util.getAreaSign(world, nspos);
			if (ao == null) {
				Variables.ignoresignsperworld.get(world).add(nspos);
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
	
	@SubscribeEvent
	public void onSignBreak(BlockEvent.BreakEvent e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BlockState state = e.getState();
		if (Util.isSignBlock(state.getBlock())) {
			BlockPos signpos = e.getPos();
			if (Variables.areasperworld.get(world).containsKey(signpos)) {
				AreaObject ao = Variables.areasperworld.get(world).get(signpos);
				List<PlayerEntity> playersinside = ao.containsplayers;
				for (PlayerEntity player : playersinside) {
					Util.areaChangeMessage(player, StringFunctions.capitalizeFirst(ao.areaname) + " no longer exists.", ao.customrgb);
				}
				
				Variables.areasperworld.get(world).remove(signpos);
			}
			if (Variables.ignoresignsperworld.get(world).contains(signpos)) {
				Variables.ignoresignsperworld.get(world).remove(signpos);
			}
		}
	}
}
