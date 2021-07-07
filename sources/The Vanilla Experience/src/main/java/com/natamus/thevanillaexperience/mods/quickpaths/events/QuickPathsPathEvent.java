/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.quickpaths.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.NumberFunctions;
import com.natamus.collective.functions.StringFunctions;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class QuickPathsPathEvent {
	private static HashMap<String, BlockPos> playernamelastpos = new HashMap<String, BlockPos>();
	private static HashMap<BlockPos, Pair<Date, List<BlockPos>>> lastpath = new HashMap<BlockPos, Pair<Date, List<BlockPos>>>();
	
	private static int currenttick = 6000;
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent e) {
		if (e.phase.equals(Phase.END)) {
			if (currenttick != 0) {
				currenttick -= 1;
				return;
			}
			currenttick = 6000;
			
        	Date now = new Date();
        	
        	List<BlockPos> toremove = new ArrayList<BlockPos>();
        	HashMap<BlockPos, Pair<Date, List<BlockPos>>> loop = new HashMap<BlockPos, Pair<Date, List<BlockPos>>>(lastpath);
        	for (BlockPos key : loop.keySet()) {
        		
        		Date pathdate = loop.get(key).getFirst();
        		long ms = (now.getTime()-pathdate.getTime());
        		if (ms > 300000) {
        			toremove.add(key);
        		}
        	}
        	
        	for (BlockPos tr : toremove) {
        		lastpath.remove(tr);
        	}
		}
	}
	
	@SubscribeEvent
	public void onRightClickGrass(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (!hand.getToolTypes().contains(ToolType.SHOVEL)) {
			return;
		}
		
		Date now = new Date();
		PlayerEntity player = e.getPlayer();
		BlockPos targetpos = e.getPos();
		Block block = world.getBlockState(targetpos).getBlock();
		
		if (block.equals(Blocks.GRASS_PATH)) {
			if (lastpath.containsKey(targetpos)) {
				e.setCanceled(true);
				
				int count = 0;
				Pair<Date, List<BlockPos>> pair = lastpath.get(targetpos);
				
				long ms = (now.getTime()-pair.getFirst().getTime());
				if (ms < 300000) {
					for (BlockPos pathpos : pair.getSecond()) {
						if (world.getBlockState(pathpos).getBlock().equals(Blocks.GRASS_PATH) && world.getBlockState(pathpos.immutable().above()).getBlock().equals(Blocks.AIR)) {
							world.setBlockAndUpdate(pathpos, Blocks.GRASS_BLOCK.defaultBlockState());
							count+=1;
						}
					}
				}
				
				lastpath.remove(targetpos);
				StringFunctions.sendMessage(player, "[Quick Paths] " + count + " grass blocks restored.", TextFormatting.DARK_GREEN);
				return;
			}
		}
		else if (!block.equals(Blocks.GRASS_BLOCK)) {
			return;
		}
		
		if (hand.getDamageValue() >= hand.getMaxDamage()-1 && player.isCrouching()) {
			e.setCanceled(true);
			StringFunctions.sendMessage(player, "[Quick Paths] Your shovel is too damaged to create paths.", TextFormatting.RED);
			return;
		}
		
		String playername = player.getName().getString();
		if (playernamelastpos.containsKey(playername) && !player.isCrouching()) {
			BlockPos lastpos = playernamelastpos.get(playername);
			
			boolean movex = true;
			int difx = lastpos.getX()-targetpos.getX();
			int difz = lastpos.getZ()-targetpos.getZ();
			int begindifx = difx;
			int begindifz = difz;
			
			List<Pair<Integer, Integer>> xzset = new ArrayList<Pair<Integer, Integer>>();
			List<BlockPos> pathpositions = new ArrayList<BlockPos>(Arrays.asList(lastpos));
			for (int lyd = lastpos.getY()-10; lyd < lastpos.getY()+10; lyd += 1) {
				difx = begindifx;
				difz = begindifz;
				
				while (difx != 0 || difz != 0) {
					if (movex) {
						difx += NumberFunctions.moveToZero(difx);
						if (difz == 0) {
							movex = true;
						}
						else {
							movex = false;
						}
					}
					else {
						difz += NumberFunctions.moveToZero(difz);
						if (difx == 0) {
							movex = false;
						}
						else {
							movex = true;
						}				
					}
					Pair<Integer, Integer> xz = new Pair<>(targetpos.getX()+difx, targetpos.getZ()+difz);
					if (!xzset.contains(xz)) {
						BlockPos betweenpos = new BlockPos(targetpos.getX() + difx, lyd, targetpos.getZ() + difz);
						if (world.getBlockState(betweenpos).getBlock().equals(Blocks.GRASS_BLOCK) && world.getBlockState(betweenpos.immutable().above()).getBlock().equals(Blocks.AIR)) {
							world.setBlockAndUpdate(betweenpos, Blocks.GRASS_PATH.defaultBlockState());
							
							pathpositions.add(betweenpos.immutable());
							xzset.add(xz);
							
							if (!player.isCreative()) {
								 hand.hurt(1, world.random, null);
							}
						}
					}
				}
			}
			
			if (hand.getDamageValue() > hand.getMaxDamage()) {
				hand.setDamageValue(hand.getMaxDamage()-1);
			}
			
			lastpath.put(targetpos, new Pair<>(now, pathpositions));
			playernamelastpos.remove(playername);
			StringFunctions.sendMessage(player, "[Quick Paths] Path of " + pathpositions.size() + " blocks created. To undo, right click last clicked block again.", TextFormatting.DARK_GREEN);
		}
		else {
			if (!player.isCrouching()) {
				return;
			}
			
			e.setCanceled(true);
			world.setBlockAndUpdate(targetpos, Blocks.GRASS_PATH.defaultBlockState());
			
			if (playernamelastpos.containsKey(playername)) {
				BlockPos lastpos = playernamelastpos.get(playername);
				
				if (lastpos != targetpos) {
					if (world.getBlockState(lastpos).getBlock().equals(Blocks.GRASS_PATH)) {
						world.setBlockAndUpdate(lastpos, Blocks.GRASS_BLOCK.defaultBlockState());
					}
				}
			}
			playernamelastpos.put(playername, targetpos);
			StringFunctions.sendMessage(player, "[Quick Paths] Starting point set to " + targetpos.getX() + ", " + targetpos.getY() + ", " + targetpos.getZ() + ".", TextFormatting.DARK_GREEN);
		}
	}
}
