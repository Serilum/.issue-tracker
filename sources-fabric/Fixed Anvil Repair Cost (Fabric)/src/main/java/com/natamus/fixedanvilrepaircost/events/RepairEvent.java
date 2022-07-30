/*
 * This is the latest source code of Fixed Anvil Repair Cost.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Fixed Anvil Repair Cost ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
