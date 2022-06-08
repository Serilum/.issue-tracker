/*
 * This is the latest source code of Quick Paths.
 * Minecraft version: 1.19.0, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Quick Paths ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.quickpaths.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.NumberFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.ToolFunctions;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PathEvent {
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
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (!ToolFunctions.isShovel(hand)) {
			return;
		}
		
		Date now = new Date();
		Player player = e.getPlayer();
		BlockPos targetpos = e.getPos();
		Block block = world.getBlockState(targetpos).getBlock();
		
		if (block.equals(Blocks.DIRT_PATH)) {
			if (lastpath.containsKey(targetpos)) {
				e.setCanceled(true);
				
				int count = 0;
				Pair<Date, List<BlockPos>> pair = lastpath.get(targetpos);
				
				long ms = (now.getTime()-pair.getFirst().getTime());
				if (ms < 300000) {
					for (BlockPos pathpos : pair.getSecond()) {
						if (world.getBlockState(pathpos).getBlock().equals(Blocks.DIRT_PATH) && world.getBlockState(pathpos.immutable().above()).getBlock().equals(Blocks.AIR)) {
							world.setBlockAndUpdate(pathpos, Blocks.GRASS_BLOCK.defaultBlockState());
							count+=1;
						}
					}
				}
				
				lastpath.remove(targetpos);
				StringFunctions.sendMessage(player, "[Quick Paths] " + count + " grass blocks restored.", ChatFormatting.DARK_GREEN);
				return;
			}
		}
		else if (!block.equals(Blocks.GRASS_BLOCK)) {
			return;
		}
		
		if (hand.getDamageValue() >= hand.getMaxDamage()-1 && player.isCrouching()) {
			e.setCanceled(true);
			StringFunctions.sendMessage(player, "[Quick Paths] Your shovel is too damaged to create paths.", ChatFormatting.RED);
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
						if (world.getBlockState(betweenpos).getBlock().equals(Blocks.GRASS_BLOCK)) {
							BlockPos abovepos = betweenpos.immutable().above();
							Block aboveblock = world.getBlockState(abovepos).getBlock();
							if (!aboveblock.equals(Blocks.AIR)) {
								if (aboveblock instanceof TallGrassBlock || aboveblock instanceof FlowerBlock || aboveblock instanceof DoublePlantBlock || aboveblock instanceof DeadBushBlock || aboveblock instanceof BushBlock || aboveblock instanceof CropBlock) {
									BlockFunctions.dropBlock(world, abovepos);
								}
								else {
									return;
								}
							}
							
							world.setBlockAndUpdate(betweenpos, Blocks.DIRT_PATH.defaultBlockState());
							
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
			StringFunctions.sendMessage(player, "[Quick Paths] Path of " + pathpositions.size() + " blocks created. To undo, right click last clicked block again.", ChatFormatting.DARK_GREEN);
		}
		else {
			if (!player.isCrouching()) {
				return;
			}
			
			e.setCanceled(true);
			world.setBlockAndUpdate(targetpos, Blocks.DIRT_PATH.defaultBlockState());
			
			if (playernamelastpos.containsKey(playername)) {
				BlockPos lastpos = playernamelastpos.get(playername);
				
				if (lastpos != targetpos) {
					if (world.getBlockState(lastpos).getBlock().equals(Blocks.DIRT_PATH)) {
						world.setBlockAndUpdate(lastpos, Blocks.GRASS_BLOCK.defaultBlockState());
					}
				}
			}
			playernamelastpos.put(playername, targetpos);
			StringFunctions.sendMessage(player, "[Quick Paths] Starting point set to " + targetpos.getX() + ", " + targetpos.getY() + ", " + targetpos.getZ() + ".", ChatFormatting.DARK_GREEN);
		}
	}
}
