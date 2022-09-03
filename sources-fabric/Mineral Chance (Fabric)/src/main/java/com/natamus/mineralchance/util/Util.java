/*
 * This is the latest source code of Mineral Chance.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.mineralchance.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective_fabric.data.GlobalVariables;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Util {
	private static List<Item> overworldminerals = new ArrayList<Item>(Arrays.asList(Items.DIAMOND, Items.GOLD_NUGGET, Items.IRON_NUGGET, Items.LAPIS_LAZULI, Items.REDSTONE, Items.EMERALD));
	private static List<Item> netherminerals = new ArrayList<Item>(Arrays.asList(Items.QUARTZ, Items.GOLD_NUGGET, Items.NETHERITE_SCRAP));
	
	public static Item getRandomOverworldMineral() {
		return overworldminerals.get(GlobalVariables.random.nextInt(overworldminerals.size()));
	}
	public static Item getRandomNetherMineral() {
		return netherminerals.get(GlobalVariables.random.nextInt(netherminerals.size()));
	}
}
