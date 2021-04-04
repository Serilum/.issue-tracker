/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.16.5, mod version: 2.25.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

import java.util.List;

import com.natamus.collective.data.GlobalVariables;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFunctions {
	// START: Checks whether specificblock equals tocheckblock
	public static Boolean isSpecificBlock(Block specificblock, Block tocheckblock) {
		if (specificblock == null || tocheckblock == null) {
			return false;
		}
		if (specificblock.equals(tocheckblock)) {
			return true;
		}
		return false;
	}
	public static Boolean isSpecificBlock(Block specificblock, ItemStack tocheckitemstack) {
		if (tocheckitemstack == null) {
			return false;
		}
		Item tocheckitem = tocheckitemstack.getItem();
		if (tocheckitem == null) {
			return false;
		}
		
		Block tocheckblock = Block.getBlockFromItem(tocheckitem);
		return isSpecificBlock(specificblock, tocheckblock);
	}
	public static Boolean isSpecificBlock(Block specificblock, World world, BlockPos pos) {
		Block tocheckblock = world.getBlockState(pos).getBlock();
		return isSpecificBlock(specificblock, tocheckblock);
	}
	// END: Checks whether specificblock equals tocheckblock
	
	public static void dropBlock(World world, BlockPos pos) {
		BlockState blockstate = world.getBlockState(pos);
		TileEntity tileentity = blockstate.hasTileEntity() ? world.getTileEntity(pos) : null;
		Block.spawnDrops(blockstate, world, pos, tileentity);
		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
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
		
		Block tocheckblock = Block.getBlockFromItem(tocheckitem);
		return isOneOfBlocks(blocks, tocheckblock);
	}
	public static Boolean isOneOfBlocks(List<Block> blocks, World world, BlockPos pos) {
		Block tocheckblock = world.getBlockState(pos).getBlock();
		return isOneOfBlocks(blocks, tocheckblock);
	}
	// END: Check whether the block to check is one of the blocks in the array.
	
	// For bamboo
	public static boolean isGrowBlock(Block block) {
		BlockState state = block.getDefaultState();
		if (state.getBlock().isIn(BlockTags.BAMBOO_PLANTABLE_ON)) {
			return true;
		}
		if (GlobalVariables.growblocks.contains(block)) {
			return true;
		}
		return false;
	}
	
	public static boolean isStoneTypeBlock(Block block) {
		if (GlobalVariables.stoneblocks.contains(block)) {
			return true;
		}
		return false;
	}
	
	public static Boolean isFilledPortalFrame(BlockState blockstate) {
		Block block = blockstate.getBlock();
		if (!block.equals(Blocks.END_PORTAL_FRAME)) {
			return false;
		}
		
		return blockstate.get(EndPortalFrameBlock.EYE);
	}
	
	public static String blockToReadableString(Block block, int amount) {
		String blockstring = "";
		String[] blockspl = block.getTranslationKey().replace("block.", "").split("\\.");
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