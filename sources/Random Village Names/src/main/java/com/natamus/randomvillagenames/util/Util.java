/*
 * This is the latest source code of Random Village Names.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.randomvillagenames.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;

public class Util {
	public static boolean isSign(Block block) {
		if (block instanceof StandingSignBlock || block instanceof WallSignBlock) {
			return true;
		}
		return false;
	}
	
	public static boolean isOverwritableBlockOrSign(Block block) {
		if (!block.equals(Blocks.AIR) && !Util.isSign(block) && (block instanceof BushBlock == false) && (block instanceof SnowLayerBlock == false)) {
			return false;
		}
		return true;
	}
}
