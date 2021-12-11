/*
 * This is the latest source code of Nutritious Milk.
 * Minecraft version: 1.18.1, mod version: 1.8.
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
public class MilkEvent {
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
			
			fs.setFoodLevel(fs.getFoodLevel() + ConfigHandler.GENERAL.hungerLevelIncrease.get());
			
			float saturation = fs.getSaturationLevel() + ConfigHandler.GENERAL.saturationLevelIncrease.get().floatValue();
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