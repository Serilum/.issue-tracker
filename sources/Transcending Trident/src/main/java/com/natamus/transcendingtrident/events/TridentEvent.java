/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.19.2, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.transcendingtrident.events;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;


@EventBusSubscriber
public class TridentEvent {
	@SubscribeEvent
	public void onItem(PlayerInteractEvent.RightClickItem e) {
		Player player = e.getEntity();
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack mainhand = player.getItemInHand(InteractionHand.MAIN_HAND);
		ItemStack offhand = player.getItemInHand(InteractionHand.OFF_HAND);
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
			if (e.getHand().equals(InteractionHand.MAIN_HAND)) {
				e.setCanceled(true);
				return;
			}
		}
		else if (offhand.getItem().equals(Items.WATER_BUCKET)) {
			if (e.getHand().equals(InteractionHand.OFF_HAND)) {
				e.setCanceled(true);
				return;
			}
		}
	}
}