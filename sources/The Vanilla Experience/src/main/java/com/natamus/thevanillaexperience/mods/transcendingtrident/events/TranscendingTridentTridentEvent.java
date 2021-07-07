/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.transcendingtrident.events;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;


@EventBusSubscriber
public class TranscendingTridentTridentEvent {
	@SubscribeEvent
	public void onItem(PlayerInteractEvent.RightClickItem e) {
		PlayerEntity player = e.getPlayer();
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack mainhand = player.getItemInHand(Hand.MAIN_HAND);
		ItemStack offhand = player.getItemInHand(Hand.OFF_HAND);
		if (!mainhand.getItem().equals(Items.TRIDENT)) {
			if (!offhand.getItem().equals(Items.TRIDENT)) {
				return;
			}
			else if (EnchantmentHelper.getRiptide(offhand) <= 0) {
				return;
			}
		}
		else if (EnchantmentHelper.getRiptide(mainhand) <= 0) {
			return;
		}
		
		if (mainhand.getItem().equals(Items.WATER_BUCKET)) {
			if (e.getHand().equals(Hand.MAIN_HAND)) {
				e.setCanceled(true);
				return;
			}
		}
		else if (offhand.getItem().equals(Items.WATER_BUCKET)) {
			if (e.getHand().equals(Hand.OFF_HAND)) {
				e.setCanceled(true);
				return;
			}
		}
	}
}