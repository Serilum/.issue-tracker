/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.betterbeaconplacement.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.natamus.collective.functions.BlockFunctions;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class BetterBeaconPlacementUtil {
	public final static List<Block> mineralblocks = new ArrayList<Block>(Arrays.asList(Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK, Blocks.EMERALD_BLOCK, Blocks.DIAMOND_BLOCK));
	
	public static BlockPos getNextLocation(Level world, BlockPos beaconpos) {
		int x = beaconpos.getX();
		int y = beaconpos.getY();
		int z = beaconpos.getZ();
		
		for (int n = 1; n <= 4; n++) {
			Iterator<BlockPos> layer = BlockPos.betweenClosedStream(x-n, y-n, z-n, x+n, y-n, z+n).iterator();
			BlockPos result = checkIterator(world, layer);
			if (result != null) {
				if (BlockFunctions.isSpecificBlock(Blocks.BEDROCK, world, result)) {
					return null;
				}
				return result;
			}
		}
		
		return null;
	}
	
	public static void processAllBaseBlocks(Level world, BlockPos beaconpos, boolean iscreative) {
		int x = beaconpos.getX();
		int y = beaconpos.getY();
		int z = beaconpos.getZ();
		
		for (int n = 1; n <= 4; n++) {
			breakBase(world, beaconpos, BlockPos.betweenClosedStream(x-n, y-n, z-n, x+n, y-n, z+n).iterator(), iscreative);
		}
	}
	
	public static void breakBase(Level world, BlockPos beaconpos, Iterator<BlockPos> it, boolean iscreative) {
		while (it.hasNext()) {
			BlockPos np = it.next();
			Block block = world.getBlockState(np).getBlock();
			if (BlockFunctions.isOneOfBlocks(mineralblocks, block)) {
				world.setBlockAndUpdate(np, Blocks.AIR.defaultBlockState());
				if (!iscreative) {
					ItemEntity ei = new ItemEntity(world, beaconpos.getX(), beaconpos.getY()+2, beaconpos.getZ(), new ItemStack(block, 1));
					world.addFreshEntity(ei);
				}
			}
		}
	}
	
	private static BlockPos checkIterator(Level world, Iterator<BlockPos> it) {
		while (it.hasNext()) {
			BlockPos np = it.next();
			Block block = world.getBlockState(np).getBlock();
			if (!BlockFunctions.isOneOfBlocks(mineralblocks, block)) {
				return np.immutable();
			}
		}
		return null;
	}
}
