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
import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.PlayerFunctions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AirEvent {
	@SubscribeEvent
	public void onBottleClick(PlayerInteractEvent.RightClickItem e) {
		Player player = e.getEntity();
		if (!player.isInWater()) {
			return;
		}
		
		ItemStack stack = e.getItemStack();
		Item stackitem = stack.getItem();
		if (!stackitem.equals(Items.GLASS_BOTTLE)) {
			if (ConfigHandler.GENERAL.disableWaterConsumptionUnderwater.get()) {
				if (stackitem.equals(Items.POTION)) {
					if (PotionUtils.getPotion(stack).equals(Potions.WATER)) {
						if (player.isUnderWater()) {
							e.setCanceled(true);
						}
					}
				}
			}
			return;
		}
		
		int maxair = player.getMaxAirSupply();
		int air = player.getAirSupply();
		
		if (air >= maxair) {
			return;
		}
		
		int newair = air + ConfigHandler.GENERAL.amountOfAirInBottles.get();
		if (newair > maxair) {
			newair = maxair;
		}
		
		player.setAirSupply(newair);
		
		InteractionHand otherhand = PlayerFunctions.getOtherHand(e.getHand());
		ItemStack otherstack = player.getItemInHand(otherhand);
		Item otheritem = otherstack.getItem();
		
		if (!Util.firetypeitems.contains(otheritem)) {
			return;
		}
		
		Inventory inv = player.getInventory();
		for (int i=35; i > 0; i--) {
			ItemStack slot = inv.getItem(i);
			Item slotitem = slot.getItem();
			if (slotitem.equals(Items.POTION)) {
				if (PotionUtils.getPotion(slot).equals(Potions.WATER)) {
					slot.shrink(1);
					
					double num = GlobalVariables.random.nextDouble();
					if (num > ConfigHandler.GENERAL.chanceGlassBottleBreaksWithFireTypeInOffhand.get()) {
						ItemFunctions.giveOrDropItemStack(player, new ItemStack(Items.GLASS_BOTTLE, 1));
					}
					
					break;
				}
			}
		}
	}
}
