/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

package com.natamus.thevanillaexperience.mods.mineralchance.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective.data.GlobalVariables;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class MineralChanceUtil {
	private static List<Block> stoneblocks = new ArrayList<Block>(Arrays.asList(Blocks.STONE, Blocks.ANDESITE, Blocks.GRANITE, Blocks.DIORITE, Blocks.NETHERRACK));
	private static List<Block> netherstoneblocks = new ArrayList<Block>(Arrays.asList(Blocks.NETHERRACK));
	private static List<Item> overworldminerals = new ArrayList<Item>(Arrays.asList(Items.DIAMOND, Items.GOLD_NUGGET, Items.IRON_NUGGET, Items.LAPIS_LAZULI, Items.REDSTONE, Items.EMERALD));
	private static List<Item> netherminerals = new ArrayList<Item>(Arrays.asList(Items.QUARTZ, Items.GOLD_NUGGET, Items.NETHERITE_SCRAP)); // netherite scrap
	
	public static boolean isStoneBlock(Block block) {
		if (stoneblocks.contains(block)) {
			return true;
		}
		return false;
	}
	
	public static boolean isNetherStoneBlock(Block block) {
		if (netherstoneblocks.contains(block)) {
			return true;
		}
		return false;
	}
	
	public static Item getRandomOverworldMineral() {
		return overworldminerals.get(GlobalVariables.random.nextInt(overworldminerals.size()));
	}
	public static Item getRandomNetherMineral() {
		return netherminerals.get(GlobalVariables.random.nextInt(netherminerals.size()));
	}
}
