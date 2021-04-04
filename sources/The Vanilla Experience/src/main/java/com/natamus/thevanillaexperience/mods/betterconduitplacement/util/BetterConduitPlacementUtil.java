/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

package com.natamus.thevanillaexperience.mods.betterconduitplacement.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.natamus.collective.functions.BlockFunctions;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BetterConduitPlacementUtil {
	public final static List<Block> conduitblocks = new ArrayList<Block>(Arrays.asList(Blocks.PRISMARINE, Blocks.DARK_PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.SEA_LANTERN));
	
	public static BlockPos getNextLocation(World world, BlockPos conduitpos) {
		int n;
		for (n = 0; n < 42; n++) {
			BlockPos npos = getNextConduitPosition(conduitpos, n);
			Block npblock = world.getBlockState(npos).getBlock();
			if (!BlockFunctions.isOneOfBlocks(conduitblocks, npblock)) {
				if (npblock.equals(Blocks.BEDROCK)) {
					return null;
				}
				return npos.toImmutable();
			}
		}
		
		if (n == 42) {
			Iterator<BlockPos> it = BlockPos.getAllInBoxMutable(conduitpos.getX()-1, conduitpos.getY()-1, conduitpos.getZ()-1, conduitpos.getX()+1, conduitpos.getY()+1, conduitpos.getZ()+1).iterator();
			while (it.hasNext()) {
				BlockPos np = it.next();
				Block nb = world.getBlockState(np).getBlock();
				if (!nb.equals(Blocks.WATER) && !nb.equals(Blocks.CONDUIT) && !BlockFunctions.isOneOfBlocks(conduitblocks, nb)) {
					world.setBlockState(np, Blocks.WATER.getDefaultState());
				}
			}
		}
		
		return null;
	}
	
	public static void destroyAllConduitBlocks(World world, BlockPos conduitpos) {
		for (int n = 0; n < 42; n++) {
			BlockPos npos = getNextConduitPosition(conduitpos, n);
			Block block = world.getBlockState(npos).getBlock();
			if (BlockFunctions.isOneOfBlocks(conduitblocks, block)) {
				world.setBlockState(npos, Blocks.WATER.getDefaultState());
				ItemEntity ei = new ItemEntity(world, conduitpos.getX(), conduitpos.getY()+1, conduitpos.getZ(), new ItemStack(block, 1));
				world.addEntity(ei);
			}
		}
	}
	
	private static BlockPos getNextConduitPosition(BlockPos conduitpos, int count) {
		switch(count) {
			case 0:
				return conduitpos.up(2).east(2);
			case 1:
				return conduitpos.up(2).east(1);
			case 2:
				return conduitpos.up(2);
			case 3:
				return conduitpos.up(2).west(1);
			case 4:
				return conduitpos.up(2).west(2);
			case 5:
				return conduitpos.up(1).west(2);
			case 6:
				return conduitpos.west(2);
			case 7:
				return conduitpos.down(1).west(2);
			case 8:
				return conduitpos.down(2).west(2);
			case 9:
				return conduitpos.down(2).west(1);
			case 10:
				return conduitpos.down(2);
			case 11:
				return conduitpos.down(2).east(1);
			case 12:
				return conduitpos.down(2).east(2);
			case 13:
				return conduitpos.down(1).east(2);
			case 14:
				return conduitpos.east(2);
			case 15:
				return conduitpos.up(1).east(2);
			case 16:
				return conduitpos.up(2).north(1);
			case 17:
				return conduitpos.up(2).north(2);
			case 18:
				return conduitpos.up(1).north(2);
			case 19:
				return conduitpos.north(2);
			case 20:
				return conduitpos.down(1).north(2);
			case 21:
				return conduitpos.down(2).north(2);
			case 22:
				return conduitpos.down(2).north(1);
			case 23:
				return conduitpos.down(2).south(1);
			case 24:
				return conduitpos.down(2).south(2);
			case 25:
				return conduitpos.down(1).south(2);
			case 26:
				return conduitpos.south(2);
			case 27:
				return conduitpos.up(1).south(2);
			case 28:
				return conduitpos.up(2).south(2);
			case 29:
				return conduitpos.up(2).south(1);
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
