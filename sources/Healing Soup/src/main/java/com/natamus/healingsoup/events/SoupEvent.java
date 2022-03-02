/*
 * This is the latest source code of Healing Soup.
 * Minecraft version: 1.18.2, mod version: 2.4.
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
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		InteractionHand hand = e.getHand();
		Player player = e.getPlayer();
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