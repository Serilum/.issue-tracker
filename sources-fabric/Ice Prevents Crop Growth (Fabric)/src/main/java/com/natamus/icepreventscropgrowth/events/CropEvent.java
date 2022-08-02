/*
 * This is the latest source code of Ice Prevents Crop Growth.
 * Minecraft version: 1.19.1, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
