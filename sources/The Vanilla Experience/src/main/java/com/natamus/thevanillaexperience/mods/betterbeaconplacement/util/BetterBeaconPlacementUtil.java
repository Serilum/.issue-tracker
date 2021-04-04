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

package com.natamus.thevanillaexperience.mods.betterbeaconplacement.util;

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

public class BetterBeaconPlacementUtil {
	public final static List<Block> mineralblocks = new ArrayList<Block>(Arrays.asList(Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK, Blocks.EMERALD_BLOCK, Blocks.DIAMOND_BLOCK));
	
	public static BlockPos getNextLocation(World world, BlockPos beaconpos) {
		int x = beaconpos.getX();
		int y = beaconpos.getY();
		int z = beaconpos.getZ();
		
		for (int n = 1; n <= 4; n++) {
			Iterator<BlockPos> layer = BlockPos.getAllInBox(x-n, y-n, z-n, x+n, y-n, z+n).iterator();
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
	
	public static void processAllBaseBlocks(World world, BlockPos beaconpos, boolean iscreative) {
		int x = beaconpos.getX();
		int y = beaconpos.getY();
		int z = beaconpos.getZ();
		
		for (int n = 1; n <= 4; n++) {
			breakBase(world, beaconpos, BlockPos.getAllInBox(x-n, y-n, z-n, x+n, y-n, z+n).iterator(), iscreative);
		}
	}
	
	public static void breakBase(World world, BlockPos beaconpos, Iterator<BlockPos> it, boolean iscreative) {
		while (it.hasNext()) {
			BlockPos np = it.next();
			Block block = world.getBlockState(np).getBlock();
			if (BlockFunctions.isOneOfBlocks(mineralblocks, block)) {
				world.setBlockState(np, Blocks.AIR.getDefaultState());
				if (!iscreative) {
					ItemEntity ei = new ItemEntity(world, beaconpos.getX(), beaconpos.getY()+2, beaconpos.getZ(), new ItemStack(block, 1));
					world.addEntity(ei);
				}
			}
		}
	}
	
	private static BlockPos checkIterator(World world, Iterator<BlockPos> it) {
		while (it.hasNext()) {
			BlockPos np = it.next();
			Block block = world.getBlockState(np).getBlock();
			if (!BlockFunctions.isOneOfBlocks(mineralblocks, block)) {
				return np.toImmutable();
			}
		}
		return null;
	}
}
