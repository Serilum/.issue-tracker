/*
 * This is the latest source code of Better Conduit Placement.
 * Minecraft version: 1.19.x, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Better Conduit Placement ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.betterconduitplacement.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.natamus.collective_fabric.functions.BlockFunctions;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class Util {
	public final static List<Block> conduitblocks = new ArrayList<Block>(Arrays.asList(Blocks.PRISMARINE, Blocks.DARK_PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.SEA_LANTERN));
	
	public static BlockPos getNextLocation(Level world, BlockPos conduitpos) {
		int n;
		for (n = 0; n < 42; n++) {
			BlockPos npos = getNextConduitPosition(conduitpos, n);
			Block npblock = world.getBlockState(npos).getBlock();
			if (!BlockFunctions.isOneOfBlocks(conduitblocks, npblock)) {
				if (npblock.equals(Blocks.BEDROCK)) {
					return null;
				}
				return npos.immutable();
			}
		}
		
		if (n == 42) {
			Iterator<BlockPos> it = BlockPos.betweenClosed(conduitpos.getX()-1, conduitpos.getY()-1, conduitpos.getZ()-1, conduitpos.getX()+1, conduitpos.getY()+1, conduitpos.getZ()+1).iterator();
			while (it.hasNext()) {
				BlockPos np = it.next();
				Block nb = world.getBlockState(np).getBlock();
				if (!nb.equals(Blocks.WATER) && !nb.equals(Blocks.CONDUIT) && !BlockFunctions.isOneOfBlocks(conduitblocks, nb)) {
					world.setBlockAndUpdate(np, Blocks.WATER.defaultBlockState());
				}
			}
		}
		
		return null;
	}
	
	public static void destroyAllConduitBlocks(Level world, BlockPos conduitpos) {
		for (int n = 0; n < 42; n++) {
			BlockPos npos = getNextConduitPosition(conduitpos, n);
			Block block = world.getBlockState(npos).getBlock();
			if (BlockFunctions.isOneOfBlocks(conduitblocks, block)) {
				world.setBlockAndUpdate(npos, Blocks.WATER.defaultBlockState());
				ItemEntity ei = new ItemEntity(world, conduitpos.getX(), conduitpos.getY()+1, conduitpos.getZ(), new ItemStack(block, 1));
				world.addFreshEntity(ei);
			}
		}
	}
	
	private static BlockPos getNextConduitPosition(BlockPos conduitpos, int count) {
		switch(count) {
			case 0:
				return conduitpos.above(2).east(2);
			case 1:
				return conduitpos.above(2).east(1);
			case 2:
				return conduitpos.above(2);
			case 3:
				return conduitpos.above(2).west(1);
			case 4:
				return conduitpos.above(2).west(2);
			case 5:
				return conduitpos.above(1).west(2);
			case 6:
				return conduitpos.west(2);
			case 7:
				return conduitpos.below(1).west(2);
			case 8:
				return conduitpos.below(2).west(2);
			case 9:
				return conduitpos.below(2).west(1);
			case 10:
				return conduitpos.below(2);
			case 11:
				return conduitpos.below(2).east(1);
			case 12:
				return conduitpos.below(2).east(2);
			case 13:
				return conduitpos.below(1).east(2);
			case 14:
				return conduitpos.east(2);
			case 15:
				return conduitpos.above(1).east(2);
			case 16:
				return conduitpos.above(2).north(1);
			case 17:
				return conduitpos.above(2).north(2);
			case 18:
				return conduitpos.above(1).north(2);
			case 19:
				return conduitpos.north(2);
			case 20:
				return conduitpos.below(1).north(2);
			case 21:
				return conduitpos.below(2).north(2);
			case 22:
				return conduitpos.below(2).north(1);
			case 23:
				return conduitpos.below(2).south(1);
			case 24:
				return conduitpos.below(2).south(2);
			case 25:
				return conduitpos.below(1).south(2);
			case 26:
				return conduitpos.south(2);
			case 27:
				return conduitpos.above(1).south(2);
			case 28:
				return conduitpos.above(2).south(2);
			case 29:
				return conduitpos.above(2).south(1);
			case 30:
				return conduitpos.south(2).east(1);
			case 31:
				return conduitpos.south(2).east(2);
			case 32:
				return conduitpos.south(1).east(2);
			case 33:
				return conduitpos.north(1).east(2);
			case 34:
				return conduitpos.north(2).east(2);
			case 35:
				return conduitpos.north(2).east(1);
			case 36:
				return conduitpos.north(2).west(1);
			case 37:
				return conduitpos.north(2).west(2);
			case 38:
				return conduitpos.north(1).west(2);
			case 39:
				return conduitpos.south(1).west(2);
			case 40:
				return conduitpos.south(2).west(2);
			case 41:
				return conduitpos.south(2).west(1);
			default:
				return null;
		}
	}
}
