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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber
public class MilkEvent {
	@SubscribeEvent
	public void onDrink(LivingEntityUseItemEvent.Finish e) {
		Entity entity = e.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		
		ItemStack itemused = e.getItem();
		Item item = itemused.getItem();
		String registryname = ForgeRegistries.ITEMS.getKey(item).toString();
		if (item.equals(Items.MILK_BUCKET) || registryname.contains("milk_bucket")) {
			Player player = (Player)entity;
			FoodData fs = player.getFoodData();
			
			fs.setFoodLevel(fs.getFoodLevel() + ConfigHandler.GENERAL.hungerLevelIncrease.get());
			fs.setSaturation(fs.getSaturationLevel() + ConfigHandler.GENERAL.saturationLevelIncrease.get().floatValue());
		}
	}
}