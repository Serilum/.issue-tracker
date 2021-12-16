/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.18.x, mod version: 2.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Transcending Trident ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.transcendingtrident.events;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;


public class TridentEvent {
	public static InteractionResultHolder<ItemStack> onItem(Player player, Level world, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (world.isClientSide) {
			return InteractionResultHolder.pass(stack);
		}
		
		ItemStack mainhand = player.getItemInHand(InteractionHand.MAIN_HAND);
		ItemStack offhand = player.getItemInHand(InteractionHand.OFF_HAND);
		if (!mainhand.getItem().equals(Items.TRIDENT)) {
			if (!offhand.getItem().equals(Items.TRIDENT)) {
				return InteractionResultHolder.pass(stack);
			}
			else if (EnchantmentHelper.getRiptide(offhand) <= 0) {
				return InteractionResultHolder.pass(stack);
			}
		}
		else if (EnchantmentHelper.getRiptide(mainhand) <= 0) {
			return InteractionResultHolder.pass(stack);
		}
		
		if (mainhand.getItem().equals(Items.WATER_BUCKET)) {
			if (hand.equals(InteractionHand.MAIN_HAND)) {
				return InteractionResultHolder.fail(stack);
			}
		}
		else if (offhand.getItem().equals(Items.WATER_BUCKET)) {
			if (hand.equals(InteractionHand.OFF_HAND)) {
				return InteractionResultHolder.fail(stack);
			}
		}
		
		return InteractionResultHolder.pass(stack);
	}
}