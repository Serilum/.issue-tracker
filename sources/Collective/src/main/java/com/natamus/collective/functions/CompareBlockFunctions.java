/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.0, mod version: 4.30.
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

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.*;

public class CompareBlockFunctions {
	public static boolean blockIsInRegistryHolder(Block block, TagKey<Block> tagKey) {
		return block.builtInRegistryHolder().is(tagKey);
	}

	public static boolean isStoneBlock(Block block) {
		return blockIsInRegistryHolder(block, BlockTags.BASE_STONE_OVERWORLD);
	}

	public static boolean isNetherStoneBlock(Block block) {
		return blockIsInRegistryHolder(block, BlockTags.BASE_STONE_NETHER);
	}

	public static boolean isTreeLeaf(Block block, boolean withNetherVariants) {
		if (blockIsInRegistryHolder(block, BlockTags.LEAVES) || block instanceof LeavesBlock) {
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
		return blockIsInRegistryHolder(block, BlockTags.LOGS) || block instanceof RotatedPillarBlock;
	}

	public static boolean isSapling(Block block) {
		return blockIsInRegistryHolder(block, BlockTags.SAPLINGS) || block instanceof SaplingBlock;
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
