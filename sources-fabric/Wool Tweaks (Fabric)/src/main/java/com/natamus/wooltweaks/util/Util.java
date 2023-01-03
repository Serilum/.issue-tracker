/*
 * This is the latest source code of Wool Tweaks.
 * Minecraft version: 1.19.3, mod version: 2.5.
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

package com.natamus.wooltweaks.util;

import java.util.HashMap;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Util {	
	public static HashMap<Item, Block> woolblocks = new HashMap<Item, Block>();
	public static HashMap<Item, Block> bedblocks = new HashMap<Item, Block>();
	public static HashMap<Item, Block> carpetblocks = new HashMap<Item, Block>();
	
	public static void initiateColourMaps() {
		woolblocks.put(Items.BLACK_DYE, Blocks.BLACK_WOOL);
		woolblocks.put(Items.BLUE_DYE, Blocks.BLUE_WOOL);
		woolblocks.put(Items.BROWN_DYE, Blocks.BROWN_WOOL);
		woolblocks.put(Items.CYAN_DYE, Blocks.CYAN_WOOL);
		woolblocks.put(Items.GRAY_DYE, Blocks.GRAY_WOOL);
		woolblocks.put(Items.GREEN_DYE, Blocks.GREEN_WOOL);
		woolblocks.put(Items.LIGHT_BLUE_DYE, Blocks.LIGHT_BLUE_WOOL);
		woolblocks.put(Items.LIGHT_GRAY_DYE, Blocks.LIGHT_GRAY_WOOL);
		woolblocks.put(Items.LIME_DYE, Blocks.LIME_WOOL);
		woolblocks.put(Items.MAGENTA_DYE, Blocks.MAGENTA_WOOL);
		woolblocks.put(Items.ORANGE_DYE, Blocks.ORANGE_WOOL);
		woolblocks.put(Items.PINK_DYE, Blocks.PINK_WOOL);
		woolblocks.put(Items.PURPLE_DYE, Blocks.PURPLE_WOOL);
		woolblocks.put(Items.RED_DYE, Blocks.RED_WOOL);
		woolblocks.put(Items.YELLOW_DYE, Blocks.YELLOW_WOOL);
		woolblocks.put(Items.WHITE_DYE, Blocks.WHITE_WOOL);

		bedblocks.put(Items.BLACK_DYE, Blocks.BLACK_BED);
		bedblocks.put(Items.BLUE_DYE, Blocks.BLUE_BED);
		bedblocks.put(Items.BROWN_DYE, Blocks.BROWN_BED);
		bedblocks.put(Items.CYAN_DYE, Blocks.CYAN_BED);
		bedblocks.put(Items.GRAY_DYE, Blocks.GRAY_BED);
		bedblocks.put(Items.GREEN_DYE, Blocks.GREEN_BED);
		bedblocks.put(Items.LIGHT_BLUE_DYE, Blocks.LIGHT_BLUE_BED);
		bedblocks.put(Items.LIGHT_GRAY_DYE, Blocks.LIGHT_GRAY_BED);
		bedblocks.put(Items.LIME_DYE, Blocks.LIME_BED);
		bedblocks.put(Items.MAGENTA_DYE, Blocks.MAGENTA_BED);
		bedblocks.put(Items.ORANGE_DYE, Blocks.ORANGE_BED);
		bedblocks.put(Items.PINK_DYE, Blocks.PINK_BED);
		bedblocks.put(Items.PURPLE_DYE, Blocks.PURPLE_BED);
		bedblocks.put(Items.RED_DYE, Blocks.RED_BED);
		bedblocks.put(Items.YELLOW_DYE, Blocks.YELLOW_BED);
		bedblocks.put(Items.WHITE_DYE, Blocks.WHITE_BED);

		carpetblocks.put(Items.BLACK_DYE, Blocks.BLACK_CARPET);
		carpetblocks.put(Items.BLUE_DYE, Blocks.BLUE_CARPET);
		carpetblocks.put(Items.BROWN_DYE, Blocks.BROWN_CARPET);
		carpetblocks.put(Items.CYAN_DYE, Blocks.CYAN_CARPET);
		carpetblocks.put(Items.GRAY_DYE, Blocks.GRAY_CARPET);
		carpetblocks.put(Items.GREEN_DYE, Blocks.GREEN_CARPET);
		carpetblocks.put(Items.LIGHT_BLUE_DYE, Blocks.LIGHT_BLUE_CARPET);
		carpetblocks.put(Items.LIGHT_GRAY_DYE, Blocks.LIGHT_GRAY_CARPET);
		carpetblocks.put(Items.LIME_DYE, Blocks.LIME_CARPET);
		carpetblocks.put(Items.MAGENTA_DYE, Blocks.MAGENTA_CARPET);
		carpetblocks.put(Items.ORANGE_DYE, Blocks.ORANGE_CARPET);
		carpetblocks.put(Items.PINK_DYE, Blocks.PINK_CARPET);
		carpetblocks.put(Items.PURPLE_DYE, Blocks.PURPLE_CARPET);
		carpetblocks.put(Items.RED_DYE, Blocks.RED_CARPET);
		carpetblocks.put(Items.YELLOW_DYE, Blocks.YELLOW_CARPET);
		carpetblocks.put(Items.WHITE_DYE, Blocks.WHITE_CARPET);
	}
}
