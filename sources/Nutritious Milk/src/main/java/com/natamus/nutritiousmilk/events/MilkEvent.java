/*
 * This is the latest source code of Nutritious Milk.
 * Minecraft version: 1.16.5, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Nutritious Milk ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.nutritiousmilk.events;

import java.lang.reflect.Field;

import com.natamus.nutritiousmilk.config.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MilkEvent {
	public static Field foodStats_foodSaturationLevel = null;
	
	@SubscribeEvent
	public void onDrink(LivingEntityUseItemEvent.Finish e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		if (entity instanceof PlayerEntity == false) {
			return;
		}
		
		ItemStack itemused = e.getItem();
		Item item = itemused.getItem();
		String registryname = item.getRegistryName().toString();
		if (item.equals(Items.MILK_BUCKET) || registryname.contains("milk_bucket")) {
			PlayerEntity player = (PlayerEntity)entity;
			FoodStats fs = player.getFoodData();
			
			fs.setFoodLevel(fs.getFoodLevel() + ConfigHandler.GENERAL.hungerLevelIncrease.get());
			
			float saturation = fs.getSaturationLevel() + ConfigHandler.GENERAL.saturationLevelIncrease.get().floatValue();
			if (player instanceof ServerPlayerEntity) {
				if (foodStats_foodSaturationLevel == null) {
					for (Field field : FoodStats.class.getDeclaredFields()) {
						if (field.toString().contains("foodSaturationLevel") || field.toString().contains("saturationLevel")) {
							foodStats_foodSaturationLevel = field;
							break;
						}
					}
					if (foodStats_foodSaturationLevel == null) {
						return;
					}
					foodStats_foodSaturationLevel.setAccessible(true);
				}
				
				try {
					foodStats_foodSaturationLevel.set(player, saturation);
				} catch (Exception ex) { }
				return;
			}
			fs.setSaturation(saturation);
		}
	}
}