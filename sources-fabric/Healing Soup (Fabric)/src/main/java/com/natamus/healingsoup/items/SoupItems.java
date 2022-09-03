/*
 * This is the latest source code of Healing Soup.
 * Minecraft version: 1.19.2, mod version: 2.8.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.healingsoup.items;

import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class SoupItems {
	public static List<Item> soups = new ArrayList<Item>();

	public static Item MUSHROOM_SOUP = new BowlFoodItem((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_FOOD).food(SoupFoods.MUSHROOM_SOUP));
	public static Item CACTUS_SOUP = new BowlFoodItem((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_FOOD).food(SoupFoods.CACTUS_SOUP));
	public static Item CHOCOLATE_MILK = new BowlFoodItem((new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_FOOD).food(SoupFoods.CHOCOLATE_MILK));
}
