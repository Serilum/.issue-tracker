/*
 * This is the latest source code of Fixed Anvil Repair Cost.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.fixedanvilrepaircost;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveAnvilEvents;
import com.natamus.fixedanvilrepaircost.config.ConfigHandler;
import com.natamus.fixedanvilrepaircost.events.RepairEvent;
import com.natamus.fixedanvilrepaircost.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveAnvilEvents.ANVIL_CHANGE.register((AnvilMenu anvilmenu, ItemStack left, ItemStack right, ItemStack output, String itemName, int baseCost, Player player) -> {
			return RepairEvent.onRepairEvent(anvilmenu, left, right, output, itemName, baseCost, player);
		});
	}
}
