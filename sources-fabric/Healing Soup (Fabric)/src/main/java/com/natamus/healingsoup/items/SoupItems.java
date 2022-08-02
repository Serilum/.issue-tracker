/*
 * This is the latest source code of Healing Soup.
 * Minecraft version: 1.19.1, mod version: 2.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
