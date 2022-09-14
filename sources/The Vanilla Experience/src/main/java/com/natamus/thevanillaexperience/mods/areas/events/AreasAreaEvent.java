/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.areas.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.thevanillaexperience.mods.areas.config.AreasConfigHandler;
import com.natamus.thevanillaexperience.mods.areas.objects.AreaObject;
import com.natamus.thevanillaexperience.mods.areas.objects.AreasVariables;
import com.natamus.thevanillaexperience.mods.areas.util.AreasUtil;
import com.natamus.collective.functions.FABFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;

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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AreasAreaEvent {
	int tickdelay = 20;
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		if (!AreasVariables.areasperworld.containsKey(world)) {
			AreasVariables.areasperworld.put(world, new HashMap<BlockPos, AreaObject>());
			AreasVariables.ignoresignsperworld.put(world, new CopyOnWriteArrayList<BlockPos>());
			
			AreasVariables.checkifshouldignoreperworld.put(world, new CopyOnWriteArrayList<BlockPos>());
			AreasVariables.ignoremap.put(world, new HashMap<BlockPos, Integer>());
		}
	}
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent e) {
		if (!e.phase.equals(Phase.START)) {
			return;
		}
		
		if (tickdelay > 0) {
			tickdelay -= 1;
			return;
		}
		tickdelay = 20;
		
		for (Level world : AreasVariables.checkifshouldignoreperworld.keySet()) {
			for (BlockPos pos : AreasVariables.checkifshouldignoreperworld.get(world)) {
				if (AreasVariables.areasperworld.get(world).containsKey(pos)) {
					AreasVariables.checkifshouldignoreperworld.get(world).remove(pos);
					AreasVariables.ignoremap.get(world).remove(pos);
					continue;
				}
				
				int checkleft = AreasVariables.ignoremap.get(world).get(pos);
				if (checkleft <= 0) {
					AreasVariables.checkifshouldignoreperworld.get(world).remove(pos);
					AreasVariables.ignoremap.get(world).remove(pos);
					
					AreasVariables.ignoresignsperworld.get(world).add(pos);
					continue;
				}
				
				AreasVariables.ignoremap.get(world).put(pos, checkleft-1);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
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
		List<BlockPos> ignoresigns = AreasVariables.ignoresignsperworld.get(world);
		
		List<BlockPos> nearbysigns = FABFunctions.getAllTileEntityPositionsNearbyEntity(BlockEntityType.SIGN, AreasConfigHandler.GENERAL.radiusAroundPlayerToCheckForSigns.get(), world, player);
		for (BlockPos nspos : nearbysigns) {
			if (ignoresigns.contains(nspos)) {
				continue;
			}
			
			AreaObject ao = AreasUtil.getAreaSign(world, nspos);
			if (ao == null) {
				if (!AreasVariables.checkifshouldignoreperworld.get(world).contains(nspos)) {
					AreasVariables.checkifshouldignoreperworld.get(world).add(nspos);
					AreasVariables.ignoremap.get(world).put(nspos, 10);
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
					AreasUtil.enterArea(ao, player, false);
					return;
				}
				
				AreasUtil.enterArea(ao, player, true);
			}
			else if (ao.containsplayers.contains(player)){
				if (Collections.frequency(enteredareas, ao.areaname) > 1) {
					AreasUtil.exitArea(ao, player, false);
					return;
				}
				
				AreasUtil.exitArea(ao, player, true);
			}
		}
	}
	
	@SubscribeEvent
	public void onSignBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BlockState state = e.getState();
		if (AreasUtil.isSignBlock(state.getBlock())) {
			BlockPos signpos = e.getPos();
			if (AreasVariables.areasperworld.get(world).containsKey(signpos)) {
				AreaObject ao = AreasVariables.areasperworld.get(world).get(signpos);
				List<Player> playersinside = ao.containsplayers;
				for (Player player : playersinside) {
					AreasUtil.areaChangeMessage(player, StringFunctions.capitalizeFirst(ao.areaname) + " no longer exists.", ao.customrgb);
				}
				
				AreasVariables.areasperworld.get(world).remove(signpos);
			}
			if (AreasVariables.ignoresignsperworld.get(world).contains(signpos)) {
				AreasVariables.ignoresignsperworld.get(world).remove(signpos);
			}
			
			if (AreasVariables.checkifshouldignoreperworld.get(world).contains(signpos)) {
				AreasVariables.checkifshouldignoreperworld.get(world).remove(signpos);
			}
			if (AreasVariables.ignoremap.get(world).containsKey(signpos)) {
				AreasVariables.ignoremap.get(world).remove(signpos);
			}
		}
	}
}
