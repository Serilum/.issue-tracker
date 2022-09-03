/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.nutritiousmilk.events;

import java.lang.reflect.Field;

import com.natamus.thevanillaexperience.mods.nutritiousmilk.config.NutritiousMilkConfigHandler;

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

@EventBusSubscriber
public class NutritiousMilkMilkEvent {
	private static Field foodStats_foodSaturationLevel = ObfuscationReflectionHelper.findField(FoodData.class, "f_38697_"); // saturationLevel
	
	@SubscribeEvent
	public void onDrink(LivingEntityUseItemEvent.Finish e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		if (entity instanceof Player == false) {
			return;
		}
		
		ItemStack itemused = e.getItem();
		Item item = itemused.getItem();
		String registryname = item.getRegistryName().toString();
		if (item.equals(Items.MILK_BUCKET) || registryname.contains("milk_bucket")) {
			Player player = (Player)entity;
			FoodData fs = player.getFoodData();
			
			fs.setFoodLevel(fs.getFoodLevel() + NutritiousMilkConfigHandler.GENERAL.hungerLevelIncrease.get());
			
			float saturation = fs.getSaturationLevel() + NutritiousMilkConfigHandler.GENERAL.saturationLevelIncrease.get().floatValue();
			if (player instanceof ServerPlayer) {
				try {
					foodStats_foodSaturationLevel.set(player, saturation);
				} catch (Exception ex) { }
				return;
			}
			
			fs.setSaturation(saturation);
		}
	}
}