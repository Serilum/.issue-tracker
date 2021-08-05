/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.randomvillagenames.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallSignBlock;

public class RandomVillageNamesUtil {
	public static boolean isSign(Block block) {
		if (block instanceof StandingSignBlock || block instanceof WallSignBlock) {
			return true;
		}
		return false;
	}
	
	public static boolean isOverwritableBlockOrSign(Block block) {
		if (!block.equals(Blocks.AIR) && !RandomVillageNamesUtil.isSign(block) && (block instanceof BushBlock == false) && (block instanceof SnowBlock == false)) {
			return false;
		}
		return true;
	}
}
