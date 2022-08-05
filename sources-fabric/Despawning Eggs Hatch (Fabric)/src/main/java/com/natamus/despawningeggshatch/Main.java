/*
 * This is the latest source code of Despawning Eggs Hatch.
 * Minecraft version: 1.19.2, mod version: 2.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.despawningeggshatch;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveItemEvents;
import com.natamus.despawningeggshatch.config.ConfigHandler;
import com.natamus.despawningeggshatch.events.EggEvent;
import com.natamus.despawningeggshatch.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveItemEvents.ON_ITEM_EXPIRE.register((ItemEntity itemEntity, ItemStack itemStack) -> {
			EggEvent.onItemExpire(itemEntity, itemStack);
		});
	}
}
