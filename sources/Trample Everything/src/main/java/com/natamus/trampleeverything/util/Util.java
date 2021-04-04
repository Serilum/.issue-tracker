/*
 * This is the latest source code of Trample Everything.
 * Minecraft version: 1.16.5, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Trample Everything ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.trampleeverything.util;

import com.natamus.collective.functions.BlockFunctions;
import com.natamus.trampleeverything.config.ConfigHandler;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.BambooSaplingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.DeadBushBlock;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.FungusBlock;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.block.MushroomBlock;
import net.minecraft.block.NetherRootsBlock;
import net.minecraft.block.NetherSproutsBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SeaGrassBlock;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Util {
	public static void trampleCheck(World world, BlockPos pos, Block block) {
		if (block instanceof FarmlandBlock) {
			pos = pos.up().toImmutable();
			block = world.getBlockState(pos).getBlock();
		}
		
		if (!isTrampleBlock(block)) {
			return;
		}
		
		if (ConfigHandler.GENERAL._enableTrampledBlockItems.get()) {
			BlockFunctions.dropBlock(world, pos);
		}
		else {
			world.destroyBlock(pos, false);
		}
	}
	
	private static boolean isTrampleBlock(Block block) {
		if (block instanceof SnowBlock) {
			if (ConfigHandler.GENERAL.trampleSnow.get()) {
				return true;
			}
			return false;			
		}
		
		// Plant-type blocks
		if (block instanceof BambooSaplingBlock) {
			if (ConfigHandler.GENERAL.trampleBambooSaplings.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof CropsBlock) {
			if (ConfigHandler.GENERAL.trampleCrops.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof DeadBushBlock) {
			if (ConfigHandler.GENERAL.trampleDeadBushes.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof DoublePlantBlock) {
			if (ConfigHandler.GENERAL.trampleDoublePlants.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof FlowerBlock) {
			if (ConfigHandler.GENERAL.trampleFlowers.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof FungusBlock) {
			if (ConfigHandler.GENERAL.trampleFungi.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof LilyPadBlock) {
			if (ConfigHandler.GENERAL.trampleLilyPads.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof MushroomBlock) {
			if (ConfigHandler.GENERAL.trampleMushrooms.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof NetherRootsBlock) {
			if (ConfigHandler.GENERAL.trampleNetherRoots.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof NetherSproutsBlock) {
			if (ConfigHandler.GENERAL.trampleNetherSprouts.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof NetherWartBlock) {
			if (ConfigHandler.GENERAL.trampleNetherWart.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof SaplingBlock) {
			if (ConfigHandler.GENERAL.trampleSaplings.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof SeaGrassBlock) {
			if (ConfigHandler.GENERAL.trampleSeaGrass.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof SeaPickleBlock) {
			if (ConfigHandler.GENERAL.trampleSeaPickles.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof StemBlock || block instanceof AttachedStemBlock) {
			if (ConfigHandler.GENERAL.trampleStems.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof SugarCaneBlock) {
			if (ConfigHandler.GENERAL.trampleSugarCane.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof SweetBerryBushBlock) {
			if (ConfigHandler.GENERAL.trampleSweetBerryBushes.get()) {
				return true;
			}
			return false;
		}
		if (block instanceof TallGrassBlock) {
			if (ConfigHandler.GENERAL.trampleTallGrass.get()) {
				return true;
			}
			return false;
		}
		return false;
	}
}
