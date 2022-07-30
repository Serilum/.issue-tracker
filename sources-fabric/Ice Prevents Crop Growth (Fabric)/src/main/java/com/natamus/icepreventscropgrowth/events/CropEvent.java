/*
 * This is the latest source code of Ice Prevents Crop Growth.
 * Minecraft version: 1.19.1, mod version: 1.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Ice Prevents Crop Growth ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.icepreventscropgrowth.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective_fabric.functions.BlockFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class CropEvent {
	private final static List<Block> iceblocks = new ArrayList<Block>(Arrays.asList(Blocks.ICE, Blocks.FROSTED_ICE, Blocks.BLUE_ICE, Blocks.PACKED_ICE));
	
	public static boolean onCropGrowth(Level world, BlockPos pos, BlockState state) {
		if (world.isClientSide) {
			return true;
		}
		
		Block belowblock = world.getBlockState(pos.below(2)).getBlock();
		if (BlockFunctions.isOneOfBlocks(iceblocks, belowblock)) {
			return false;
		}
		
		return true;
	}
}
