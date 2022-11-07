/*
 * This is the latest source code of Healing Soup.
 * Minecraft version: 1.19.2, mod version: 3.0.
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

package com.natamus.healingsoup.events;

import com.natamus.healingsoup.config.ConfigHandler;
import com.natamus.healingsoup.items.SoupItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

				stats.setFoodLevel(Math.min(food, 20));
			}
			else {
				int health = (int) (player.getHealth() + ConfigHandler.GENERAL.soupHalfHeartHealAmount.get());

				player.setHealth(Math.min(health, 20));
			}
			
			player.setItemInHand(hand, bowl);
			e.setCanceled(true);
		}
	}
}