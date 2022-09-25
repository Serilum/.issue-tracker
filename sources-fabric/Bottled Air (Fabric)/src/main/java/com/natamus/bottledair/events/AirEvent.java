/*
 * This is the latest source code of Bottled Air.
 * Minecraft version: 1.19.2, mod version: 1.6.
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

package com.natamus.bottledair.events;

import com.natamus.bottledair.config.ConfigHandler;
import com.natamus.bottledair.util.Util;
import com.natamus.collective_fabric.data.GlobalVariables;
import com.natamus.collective_fabric.functions.ItemFunctions;
import com.natamus.collective_fabric.functions.PlayerFunctions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

public class AirEvent {
	public static InteractionResultHolder<ItemStack> onBottleClick(Player player, Level world, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!player.isInWater()) {
			return InteractionResultHolder.pass(stack);
		}

		Item stackitem = stack.getItem();
		if (!stackitem.equals(Items.GLASS_BOTTLE)) {
			if (ConfigHandler.disableWaterConsumptionUnderwater.getValue()) {
				if (stackitem.equals(Items.POTION)) {
					if (PotionUtils.getPotion(stack).equals(Potions.WATER)) {
						if (player.isUnderWater()) {
							return InteractionResultHolder.fail(stack);
						}
					}
				}
			}
			return InteractionResultHolder.pass(stack);
		}
		
		int maxair = player.getMaxAirSupply();
		int air = player.getAirSupply();
		
		if (air >= maxair) {
			return InteractionResultHolder.pass(stack);
		}
		
		int newair = air + ConfigHandler.amountOfAirInBottles.getValue();
		if (newair > maxair) {
			newair = maxair;
		}
		
		player.setAirSupply(newair);
		
		InteractionHand otherhand = PlayerFunctions.getOtherHand(hand);
		ItemStack otherstack = player.getItemInHand(otherhand);
		Item otheritem = otherstack.getItem();
		
		if (!Util.firetypeitems.contains(otheritem)) {
			return InteractionResultHolder.pass(stack);
		}
		
		Inventory inv = player.getInventory();
		for (int i=35; i > 0; i--) {
			ItemStack slot = inv.getItem(i);
			Item slotitem = slot.getItem();
			if (slotitem.equals(Items.POTION)) {
				if (PotionUtils.getPotion(slot).equals(Potions.WATER)) {
					slot.shrink(1);
					
					double num = GlobalVariables.random.nextDouble();
					if (num > ConfigHandler.chanceGlassBottleBreaksWithFireTypeInOffhand.getValue()) {
						ItemFunctions.giveOrDropItemStack(player, new ItemStack(Items.GLASS_BOTTLE, 1));
					}
					
					break;
				}
			}
		}
		
		return InteractionResultHolder.pass(stack);
	}
}
