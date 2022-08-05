/*
 * This is the latest source code of Fixed Anvil Repair Cost.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.fixedanvilrepaircost.events;

import com.natamus.collective_fabric.objects.Triplet;
import com.natamus.fixedanvilrepaircost.config.ConfigHandler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RepairEvent {
	public static Triplet<Integer, Integer, ItemStack> onRepairEvent(AnvilMenu anvilmenu, ItemStack leftstack, ItemStack rightstack, ItemStack outputstack, String itemName, int baseCost, Player player) {
		int newlevelcost = -1;
		int newmaterialcost = -1;
		ItemStack newoutput = null;
		
		if (!rightstack.getItem().equals(Items.ENCHANTED_BOOK) && !leftstack.getItem().equals(rightstack.getItem())) {
			Item leftitem = leftstack.getItem();
			if (!leftitem.isValidRepairItem(leftstack, rightstack)) {
				return null;
			}
			
			int levelcost = ConfigHandler.repairCostLevelAmount.getValue();
			int materialcost = ConfigHandler.repairCostMaterialAmount.getValue();
			
			if (levelcost >= 1 || materialcost >= 1) {
				if (levelcost >= 1) {
					newlevelcost = levelcost;
				}
				if (materialcost >= 1) {
					newmaterialcost = materialcost;
					
					if (materialcost > rightstack.getCount()) {
						newoutput = ItemStack.EMPTY;
						return new Triplet<Integer, Integer, ItemStack>(newlevelcost, newmaterialcost, newoutput);
					}
				}
				
				int currentdamage = leftstack.getDamageValue();
				int maxdamage = leftstack.getMaxDamage();
				int repairamount = (int)(maxdamage * ConfigHandler.percentRepairedPerAction.getValue());
				
				currentdamage -= repairamount;
				if (currentdamage < 0) {
					currentdamage = 0;
				}
				
				ItemStack newoutputstack = leftstack.copy();
				newoutputstack.setDamageValue(currentdamage);
				
				newoutput = newoutputstack;
			}
			
			return new Triplet<Integer, Integer, ItemStack>(newlevelcost, newmaterialcost, newoutput);
		}
		
		return null;
	}
}
