/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 3.9.
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
		if (BlockTags.BASE_STONE_OVERWORLD.contains(block)) {
			return true;
		}
		return false;
	}
	
	public static boolean isNetherStoneBlock(Block block) {
		if (BlockTags.BASE_STONE_NETHER.contains(block)) {
			return true;
		}
		return false;
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
			if (block instanceof CropBlock == false && block instanceof DeadBushBlock == false && block instanceof DoublePlantBlock == false && block instanceof FlowerBlock == false && block instanceof SaplingBlock == false && block instanceof StemBlock == false && block instanceof AttachedStemBlock == false && block instanceof SweetBerryBushBlock == false && block instanceof TallGrassBlock == false) {
				return true;
			}
		}
		return false;
	}
	public static boolean isTreeLeaf(Block block) {
		return isTreeLeaf(block, true);
	}
	
	public static boolean isTreeLog(Block block) {
		if (BlockTags.LOGS.contains(block) || block instanceof RotatedPillarBlock) {
			return true;
		}
		return false;
	}
	
	public static boolean isSapling(Block block) {
		if (BlockTags.SAPLINGS.contains(block) || block instanceof SaplingBlock) {
			return true;
		}
		return false;
	}
	
	public static boolean isDirtBlock(Block block) {
		if (block.equals(Blocks.GRASS_BLOCK) || block.equals(Blocks.DIRT) || block.equals(Blocks.COARSE_DIRT) || block.equals(Blocks.PODZOL)) {
			return true;
		}
		return false;
	}
	
	public static boolean isPortalBlock(Block block) {
		if (block instanceof NetherPortalBlock || BlockFunctions.blockToReadableString(block).equals("portal placeholder")) {
			return true;
		}

		return false;
	}
	
	public static boolean isAirOrOverwritableBlock(Block block) {
		if (!block.equals(Blocks.AIR) && (block instanceof BushBlock == false) && (block instanceof SnowLayerBlock == false)) {
			return false;
		}
		return true;
	}
}
