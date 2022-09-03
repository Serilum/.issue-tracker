/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.mineralchance.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective.data.GlobalVariables;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class MineralChanceUtil {
	private static List<Block> stoneblocks = new ArrayList<Block>(Arrays.asList(Blocks.STONE, Blocks.ANDESITE, Blocks.GRANITE, Blocks.DIORITE, Blocks.NETHERRACK));
	private static List<Block> netherstoneblocks = new ArrayList<Block>(Arrays.asList(Blocks.NETHERRACK));
	private static List<Item> overworldminerals = new ArrayList<Item>(Arrays.asList(Items.DIAMOND, Items.GOLD_NUGGET, Items.IRON_NUGGET, Items.LAPIS_LAZULI, Items.REDSTONE, Items.EMERALD));
	private static List<Item> netherminerals = new ArrayList<Item>(Arrays.asList(Items.QUARTZ, Items.GOLD_NUGGET, Items.NETHERITE_SCRAP));
	
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
