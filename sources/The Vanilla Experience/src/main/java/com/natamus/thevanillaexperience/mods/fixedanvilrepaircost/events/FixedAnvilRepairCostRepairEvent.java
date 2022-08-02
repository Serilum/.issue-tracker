/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.fixedanvilrepaircost.events;

import com.natamus.thevanillaexperience.mods.fixedanvilrepaircost.config.FixedAnvilRepairCostConfigHandler;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FixedAnvilRepairCostRepairEvent {
	@SubscribeEvent
	public void onRepairEvent(AnvilUpdateEvent e) {
		ItemStack leftstack = e.getLeft();
		ItemStack rightstack = e.getRight();
		ItemStack outputstack = e.getOutput();
		
		if (!rightstack.getItem().equals(Items.ENCHANTED_BOOK) && !leftstack.getItem().equals(rightstack.getItem()) && outputstack.isEmpty()) {
			Item leftitem = leftstack.getItem();
			if (!leftitem.isValidRepairItem(leftstack, rightstack)) {
				return;
			}
			
			int levelcost = FixedAnvilRepairCostConfigHandler.GENERAL.repairCostLevelAmount.get();
			int materialcost = FixedAnvilRepairCostConfigHandler.GENERAL.repairCostMaterialAmount.get();
			
			if (levelcost >= 1 || materialcost >= 1) {
				if (levelcost >= 1) {
					e.setCost(levelcost);
				}
				if (materialcost >= 1) {
					e.setMaterialCost(materialcost);
					
					if (materialcost > rightstack.getCount()) {
						e.setCanceled(true);
						return;
					}
				}
				
				int currentdamage = leftstack.getDamageValue();
				int maxdamage = leftstack.getMaxDamage();
				int repairamount = (int)(maxdamage * FixedAnvilRepairCostConfigHandler.GENERAL.percentRepairedPerAction.get());
				
				currentdamage -= repairamount;
				if (currentdamage < 0) {
					currentdamage = 0;
				}
				
				ItemStack newoutputstack = leftstack.copy();
				newoutputstack.setDamageValue(currentdamage);
				
				e.setOutput(newoutputstack);
			}
		}
	}
}
