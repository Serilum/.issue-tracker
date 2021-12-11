/*
 * This is the latest source code of World Border.
 * Minecraft version: 1.18.1, mod version: 2.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of World Border ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.worldborder.events;

import java.util.HashMap;
import java.util.Iterator;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.worldborder.config.ConfigHandler;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BorderEvent {
	private static HashMap<String, BlockPos> lastplayerpos = new HashMap<String, BlockPos>();
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (player.tickCount % 20 != 0) {
			return;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		String dimension = serverworld.dimension().location().toString();
		
		int posx = 0;
		int negx = 0;
		int posz = 0;
		int negz = 0;
		if (dimension.equals("minecraft:overworld")) {
			if (!ConfigHandler.GENERAL.enableCustomOverworldBorder.get()) {
				return;
			}
			
			posx = ConfigHandler.BORDERS.overworldBorderPositiveX.get();
			negx = ConfigHandler.BORDERS.overworldBorderNegativeX.get();
			posz = ConfigHandler.BORDERS.overworldBorderPositiveZ.get();
			negz = ConfigHandler.BORDERS.overworldBorderNegativeZ.get();
		}
		else if (dimension.equals("minecraft:the_nether")) {
			if (!ConfigHandler.GENERAL.enableCustomNetherBorder.get()) {
				return;
			}
			
			posx = ConfigHandler.BORDERS.netherBorderPositiveX.get();
			negx = ConfigHandler.BORDERS.netherBorderNegativeX.get();
			posz = ConfigHandler.BORDERS.netherBorderPositiveZ.get();
			negz = ConfigHandler.BORDERS.netherBorderNegativeZ.get();
		}
		else if (dimension.equals("minecraft:the_end")) {
			if (!ConfigHandler.GENERAL.enableCustomEndBorder.get()) {
				return;
			}
			
			posx = ConfigHandler.BORDERS.endBorderPositiveX.get();
			negx = ConfigHandler.BORDERS.endBorderNegativeX.get();
			posz = ConfigHandler.BORDERS.endBorderPositiveZ.get();
			negz = ConfigHandler.BORDERS.endBorderNegativeZ.get();
		}
		else {
			return;
		}
		
		BlockPos ppos = player.blockPosition();
		boolean altered = false;
		boolean shouldloop = ConfigHandler.GENERAL.shouldLoopToOppositeBorder.get();
		
		int x = ppos.getX();
		int z = ppos.getZ();
		int d = ConfigHandler.GENERAL.distanceTeleportedBack.get();
		if (x <= negx) {
			if (shouldloop) {
				x = posx - d;
			}
			else {
				x = negx + d;
			}
			altered = true;
		}
		else if (x >= posx) {
			if (shouldloop) {
				x = negx + d;
			}
			else {
				x = posx - d;
			}
			altered = true;
		}
		
		if (z <= negz) {
			if (shouldloop) {
				z = posz - d;
			}
			else {
				z = negz + d;
			}
			altered = true;
		}
		else if (z >= posz) {
			if (shouldloop) {
				z = negz + d;
			}
			else {
				z = posz - d;
			}
			altered = true;
		}
		
		if (altered) {
			BlockPos centerpos = new BlockPos(0, 0, 0);
			BlockPos newpos = BlockPosFunctions.getSurfaceBlockPos(serverworld, x, z);
			if ((newpos.equals(centerpos) && dimension.equals("minecraft:the_nether") || (newpos.getY() == 128 && dimension.equals("minecraft:the_nether")))) {
				Iterator<BlockPos> posaround = BlockPos.betweenClosed(x-5, 0, z-5, x+5, 128, z+5).iterator();
				while(posaround.hasNext()) {
					BlockPos around = posaround.next();
					if (world.getBlockState(around).getBlock().equals(Blocks.AIR)) {
						if (world.getBlockState(around.above()).getBlock().equals(Blocks.AIR)) {
							newpos = around.immutable();
							break;
						}
					}
				}
			}
			
			if (dimension.equals("minecraft:the_nether")) {
				Iterator<BlockPos> checkforlavablocks = BlockPos.betweenClosed(newpos.getX()-1, newpos.getY()-1, newpos.getZ()-1, newpos.getX()+1, newpos.getY()+1, newpos.getZ()+1).iterator();
				while (checkforlavablocks.hasNext()) {
					BlockPos checkforlavapos = checkforlavablocks.next();
					if (checkforlavapos.getY() > newpos.getY()-1) {
						world.setBlockAndUpdate(checkforlavapos, Blocks.AIR.defaultBlockState());
					}
					else if (world.getBlockState(checkforlavapos).getBlock().equals(Blocks.LAVA)) {
						world.setBlockAndUpdate(checkforlavapos, Blocks.OBSIDIAN.defaultBlockState());
					}
				}
			}
			
			if (newpos.getY() < 0) {
				newpos = newpos.above().immutable();
			}
			
			Entity ride = player.getVehicle();
			if (ride != null) {
				ride.ejectPassengers();
				ride.teleportTo(newpos.getX(), newpos.getY(), newpos.getZ());
			}
			
			player.teleportTo(newpos.getX(), newpos.getY(), newpos.getZ());
			
			if (shouldloop) {
				StringFunctions.sendMessage(player, ConfigHandler.GENERAL.loopBorderMessage.get(), ChatFormatting.DARK_GREEN);
			}
			else {
				StringFunctions.sendMessage(player, ConfigHandler.GENERAL.hitBorderMessage.get(), ChatFormatting.RED);
			}
		}
		else {
			boolean shouldmessage = false;
			if (x < 0) {
				if (negx - x < 0 && negx - x > -d) {
					shouldmessage = true;
				}
			}
			else {
				if (posx - x > 0 && posx - x < d) {
					shouldmessage = true;
				}
			}
			
			if (z < 0) {
				if (negz - z < 0 && negz - z > -d) {
					shouldmessage = true;
				}
			}
			else {
				if (posz - z > 0 && posz - z < d) {
					shouldmessage = true;
				}
			}
			
			if (shouldmessage) {
				String playername = player.getName().getString();
				BlockPos lastpos = ppos.immutable();
				if (lastplayerpos.containsKey(playername)) {
					lastpos = lastplayerpos.get(playername);
				}
				
				lastplayerpos.put(playername, ppos.immutable());
				if (lastpos.equals(ppos)) {
					return;
				}
				
				StringFunctions.sendMessage(player, ConfigHandler.GENERAL.nearBorderMessage.get(), ChatFormatting.YELLOW);
			}
		}
	}
}
