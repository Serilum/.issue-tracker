/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.52.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.collective_fabric.functions;

import java.util.List;

import com.natamus.collective_fabric.data.GlobalVariables;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockFunctions {
	// START: Checks whether specificblock equals tocheckblock
	public static Boolean isSpecificBlock(Block specificblock, Block tocheckblock) {
		if (specificblock == null || tocheckblock == null) {
			return false;
		}
        return specificblock.equals(tocheckblock);
    }
	public static Boolean isSpecificBlock(Block specificblock, ItemStack tocheckitemstack) {
		if (tocheckitemstack == null) {
			return false;
		}
		Item tocheckitem = tocheckitemstack.getItem();
		if (tocheckitem == null) {
			return false;
		}
		
		Block tocheckblock = Block.byItem(tocheckitem);
		return isSpecificBlock(specificblock, tocheckblock);
	}
	public static Boolean isSpecificBlock(Block specificblock, Level world, BlockPos pos) {
		Block tocheckblock = world.getBlockState(pos).getBlock();
		return isSpecificBlock(specificblock, tocheckblock);
	}
	// END: Checks whether specificblock equals tocheckblock
	
	public static void dropBlock(Level world, BlockPos pos) {
		BlockState blockstate = world.getBlockState(pos);
		BlockEntity tileentity = world.getBlockEntity(pos);
		
		Block.dropResources(blockstate, world, pos, tileentity);
		world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
	}
	
	// START: Check whether the block to check is one of the blocks in the array.
	public static Boolean isOneOfBlocks(List<Block> blocks, Block tocheckblock) {
		if (blocks.size() < 1) {
			return false;
		}
		
		for (Block specificblock : blocks) {
			if (isSpecificBlock(specificblock, tocheckblock)) {
				return true;
			}
		}
		
		return false;
	}
	public static Boolean isOneOfBlocks(List<Block> blocks, ItemStack tocheckitemstack) {
		if (tocheckitemstack == null) {
			return false;
		}
		Item tocheckitem = tocheckitemstack.getItem();
		if (tocheckitem == null) {
			return false;
		}
		
		Block tocheckblock = Block.byItem(tocheckitem);
		return isOneOfBlocks(blocks, tocheckblock);
	}
	public static Boolean isOneOfBlocks(List<Block> blocks, Level world, BlockPos pos) {
		Block tocheckblock = world.getBlockState(pos).getBlock();
		return isOneOfBlocks(blocks, tocheckblock);
	}
	// END: Check whether the block to check is one of the blocks in the array.
	
	// For bamboo
	public static boolean isGrowBlock(Block block) {
		return GlobalVariables.growblocks.contains(block);
    }
	
	public static boolean isStoneTypeBlock(Block block) {
        return GlobalVariables.stoneblocks.contains(block);
    }
	
	public static Boolean isFilledPortalFrame(BlockState blockstate) {
		Block block = blockstate.getBlock();
		if (!block.equals(Blocks.END_PORTAL_FRAME)) {
			return false;
		}
		
		return blockstate.getValue(EndPortalFrameBlock.HAS_EYE);
	}
	
	public static String blockToReadableString(Block block, int amount) {
		String blockstring;
		String[] blockspl = block.getDescriptionId().replace("block.", "").split("\\.");
		if (blockspl.length > 1) {
			blockstring = blockspl[1];
		}
		else {
			blockstring = blockspl[0];
		}
		
		blockstring = blockstring.replace("_", " ");
		if (amount > 1) {
			blockstring = blockstring + "s";
		}
		
		return blockstring;
	}
	public static String blockToReadableString(Block block) {
		return blockToReadableString(block, 1);
	}
}