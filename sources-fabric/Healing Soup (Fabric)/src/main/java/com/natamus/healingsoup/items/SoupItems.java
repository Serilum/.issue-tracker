/*
 * This is the latest source code of Healing Soup.
 * Minecraft version: 1.18.x, mod version: 2.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Healing Soup ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
