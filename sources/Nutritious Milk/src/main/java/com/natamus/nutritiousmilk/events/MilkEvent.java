/*
 * This is the latest source code of Nutritious Milk.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.nutritiousmilk.events;

import com.natamus.nutritiousmilk.config.ConfigHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;

@EventBusSubscriber
public class MilkEvent {
	private static Field foodStats_foodSaturationLevel = ObfuscationReflectionHelper.findField(FoodData.class, "f_38697_"); // saturationLevel
	
	@SubscribeEvent
	public void onDrink(LivingEntityUseItemEvent.Finish e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
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
			
			float saturation = fs.getSaturationLevel() + ConfigHandler.GENERAL.saturationLevelIncrease.get().floatValue();
			if (player instanceof ServerPlayer) {
				try {
					foodStats_foodSaturationLevel.set(player, saturation);
				} catch (Exception ignored) { }
				return;
			}
			
			fs.setSaturation(saturation);
		}
	}
}