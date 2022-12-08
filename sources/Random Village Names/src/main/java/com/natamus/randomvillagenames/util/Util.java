/*
 * This is the latest source code of Random Village Names.
 * Minecraft version: 1.19.3, mod version: 2.0.
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

package com.natamus.randomvillagenames.util;

import com.natamus.collective.functions.FABFunctions;
import com.natamus.collective.functions.SignFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
	private static List<String> zoneprefixes = new ArrayList<String>(Arrays.asList("[na]", "[area]", "[region]", "[zone]"));

	public static boolean hasAreasSignNeaby(Level level, BlockPos pos, int radius) {
		List<BlockPos> signsaround = FABFunctions.getAllTileEntityPositionsNearbyPosition(BlockEntityType.SIGN, radius, level, pos);
		for (BlockPos signpos : signsaround) {
			BlockEntity te = level.getBlockEntity(signpos);
			if (te instanceof SignBlockEntity) {
				if (isAreasSign((SignBlockEntity) te)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isAreasSign(SignBlockEntity signentity) {
		int i = -1;
		for (String line : SignFunctions.getSignText(signentity)) {
			i += 1;

			if (i == 0 && hasZonePrefix(line)) {
				return true;
			}
			break;
		}

		return false;
	}

	private static boolean hasZonePrefix(String line) {
		for (String prefix : zoneprefixes) {
			if (line.toLowerCase().startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSign(Block block) {
		return block instanceof StandingSignBlock || block instanceof WallSignBlock;
	}
	
	public static boolean isOverwritableBlockOrSign(Block block) {
		return block.equals(Blocks.AIR) || Util.isSign(block) || (block instanceof BushBlock) || (block instanceof SnowLayerBlock);
	}
}
