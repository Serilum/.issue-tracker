/*
 * This is the latest source code of Trample Everything.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.trampleeverything.util;

import com.natamus.collective_fabric.functions.BlockFunctions;
import com.natamus.trampleeverything.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.BambooSaplingBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FungusBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.NetherSproutsBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.RootsBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.WaterlilyBlock;

public class Util {
	public static void trampleCheck(Level world, BlockPos pos, Block block) {
		if (block instanceof FarmBlock) {
			pos = pos.above().immutable();
			block = world.getBlockState(pos).getBlock();
		}
		
		if (!isTrampleBlock(block)) {
			return;
		}
		
		if (ConfigHandler._enableTrampledBlockItems) {
			BlockFunctions.dropBlock(world, pos);
		}
		else {
			world.destroyBlock(pos, false);
		}
	}
	
	private static boolean isTrampleBlock(Block block) {
		if (block instanceof SnowLayerBlock) {
			if (ConfigHandler.trampleSnow) {
				return true;
			}
			return false;			
		}
		
		// Plant-type blocks
		if (block instanceof BambooSaplingBlock) {
			if (ConfigHandler.trampleBambooSaplings) {
				return true;
			}
			return false;
		}
		if (block instanceof CropBlock) {
			if (ConfigHandler.trampleCrops) {
				return true;
			}
			return false;
		}
		if (block instanceof DeadBushBlock) {
			if (ConfigHandler.trampleDeadBushes) {
				return true;
			}
			return false;
		}
		if (block instanceof DoublePlantBlock) {
			if (ConfigHandler.trampleDoublePlants) {
				return true;
			}
			return false;
		}
		if (block instanceof FlowerBlock) {
			if (ConfigHandler.trampleFlowers) {
				return true;
			}
			return false;
		}
		if (block instanceof FungusBlock) {
			if (ConfigHandler.trampleFungi) {
				return true;
			}
			return false;
		}
		if (block instanceof WaterlilyBlock) {
			if (ConfigHandler.trampleLilyPads) {
				return true;
			}
			return false;
		}
		if (block instanceof MushroomBlock) {
			if (ConfigHandler.trampleMushrooms) {
				return true;
			}
			return false;
		}
		if (block instanceof RootsBlock) {
			if (ConfigHandler.trampleNetherRoots) {
				return true;
			}
			return false;
		}
		if (block instanceof NetherSproutsBlock) {
			if (ConfigHandler.trampleNetherSprouts) {
				return true;
			}
			return false;
		}
		if (block instanceof NetherWartBlock) {
			if (ConfigHandler.trampleNetherWart) {
				return true;
			}
			return false;
		}
		if (block instanceof SaplingBlock) {
			if (ConfigHandler.trampleSaplings) {
				return true;
			}
			return false;
		}
		if (block instanceof SeagrassBlock) {
			if (ConfigHandler.trampleSeaGrass) {
				return true;
			}
			return false;
		}
		if (block instanceof SeaPickleBlock) {
			if (ConfigHandler.trampleSeaPickles) {
				return true;
			}
			return false;
		}
		if (block instanceof StemBlock || block instanceof AttachedStemBlock) {
			if (ConfigHandler.trampleStems) {
				return true;
			}
			return false;
		}
		if (block instanceof SugarCaneBlock) {
			if (ConfigHandler.trampleSugarCane) {
				return true;
			}
			return false;
		}
		if (block instanceof SweetBerryBushBlock) {
			if (ConfigHandler.trampleSweetBerryBushes) {
				return true;
			}
			return false;
		}
		if (block instanceof TallGrassBlock) {
			if (ConfigHandler.trampleTallGrass) {
				return true;
			}
			return false;
		}
		return false;
	}
}
