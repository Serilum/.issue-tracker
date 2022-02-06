/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 4.4.
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

package com.natamus.collective_fabric.functions;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.TallGrassBlock;

public class CompareBlockFunctions {
	public static boolean isStoneBlock(Block block) {
		return BlockTags.BASE_STONE_OVERWORLD.contains(block);
	}
	
	public static boolean isNetherStoneBlock(Block block) {
		return BlockTags.BASE_STONE_NETHER.contains(block);
	}
	
	public static boolean isTreeLeaf(Block block, boolean withNetherVariants) {
		if (BlockTags.LEAVES.contains(block) || block instanceof LeavesBlock) {
			return true;
		}
		if (withNetherVariants) {
			if (block.equals(Blocks.NETHER_WART_BLOCK) || block.equals(Blocks.WARPED_WART_BLOCK) || block.equals(Blocks.SHROOMLIGHT)) {
				return true;
			}
		}
		if (block instanceof BushBlock) {
			return !(block instanceof CropBlock) && !(block instanceof DeadBushBlock) && !(block instanceof DoublePlantBlock) && !(block instanceof FlowerBlock) && !(block instanceof SaplingBlock) && !(block instanceof StemBlock) && !(block instanceof AttachedStemBlock) && !(block instanceof SweetBerryBushBlock) && !(block instanceof TallGrassBlock);
		}
		return false;
	}
	public static boolean isTreeLeaf(Block block) {
		return isTreeLeaf(block, true);
	}
	
	public static boolean isTreeLog(Block block) {
		return BlockTags.LOGS.contains(block) || block instanceof RotatedPillarBlock;
	}
	
	public static boolean isSapling(Block block) {
		return BlockTags.SAPLINGS.contains(block) || block instanceof SaplingBlock;
	}
	
	public static boolean isDirtBlock(Block block) {
		return block.equals(Blocks.GRASS_BLOCK) || block.equals(Blocks.DIRT) || block.equals(Blocks.COARSE_DIRT) || block.equals(Blocks.PODZOL);
	}
	
	public static boolean isPortalBlock(Block block) {
		return block instanceof NetherPortalBlock || BlockFunctions.blockToReadableString(block).equals("portal placeholder");
	}
	
	public static boolean isAirOrOverwritableBlock(Block block) {
		return block.equals(Blocks.AIR) || (block instanceof BushBlock) || (block instanceof SnowLayerBlock);
	}
}
