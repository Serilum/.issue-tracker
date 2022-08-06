/*
 * This is the latest source code of Healing Soup.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.healingsoup.events;

import com.natamus.healingsoup.config.ConfigHandler;
import com.natamus.healingsoup.items.SoupItems;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SoupEvent {
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickItem e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		InteractionHand hand = e.getHand();
		Player player = e.getEntity();
		ItemStack handstack = e.getItemStack();
		Item handitem = handstack.getItem();
		
		if (handitem.equals(Items.MUSHROOM_STEW) || SoupItems.soups.contains(handitem)) {
			if (handstack.getCount() > 1) {
				e.setCanceled(true);
				return;
			}
			
			ItemStack bowl = new ItemStack(Items.BOWL, 1);
			
			FoodData stats = player.getFoodData();
			if (player.getHealth() == 20) {
				if (stats.getFoodLevel() == 20) {
					return;
				}	

				int food = stats.getFoodLevel() + ConfigHandler.GENERAL.soupHalfHeartHealAmount.get();

				if (food > 20) {
					stats.setFoodLevel(20);
				}
				else {
					stats.setFoodLevel(food);
				}
			}
			else {
				int health = (int) (player.getHealth() + ConfigHandler.GENERAL.soupHalfHeartHealAmount.get());

				if (health > 20) {
					player.setHealth(20);
				}
				else {
					player.setHealth(health);
				}
			}
			
			player.setItemInHand(hand, bowl);
			e.setCanceled(true);
		}
	}
}