/*
 * This is the latest source code of Wool Tweaks.
 * Minecraft version: 1.16.5, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Wool Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.wooltweaks.util;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

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
	}
}
