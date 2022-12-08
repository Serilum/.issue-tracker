/*
 * This is the latest source code of Fixed Anvil Repair Cost.
 * Minecraft version: 1.19.3, mod version: 1.9.
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

package com.natamus.fixedanvilrepaircost.events;

import com.natamus.fixedanvilrepaircost.config.ConfigHandler;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RepairEvent {
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
			
			int levelcost = ConfigHandler.GENERAL.repairCostLevelAmount.get();
			int materialcost = ConfigHandler.GENERAL.repairCostMaterialAmount.get();
			
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
				int repairamount = (int)(maxdamage * ConfigHandler.GENERAL.percentRepairedPerAction.get());
				
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
