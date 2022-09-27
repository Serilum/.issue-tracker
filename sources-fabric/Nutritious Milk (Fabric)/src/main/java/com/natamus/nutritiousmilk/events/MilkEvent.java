/*
 * This is the latest source code of Nutritious Milk.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.nutritiousmilk.events;

import com.natamus.nutritiousmilk.config.ConfigHandler;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MilkEvent {
	public static void onDrink(Player player, ItemStack usedItem, ItemStack newItem, InteractionHand hand) {
		Item item = usedItem.getItem();
		String registryname = Registry.ITEM.getKey(item).toString();
		if (item.equals(Items.MILK_BUCKET) || registryname.contains("milk_bucket")) {
			FoodData fs = player.getFoodData();
			
			fs.setFoodLevel(fs.getFoodLevel() + ConfigHandler.hungerLevelIncrease.getValue());
			fs.setSaturation(fs.getSaturationLevel() + ConfigHandler.saturationLevelIncrease.getValue().floatValue());
		}
	}
}