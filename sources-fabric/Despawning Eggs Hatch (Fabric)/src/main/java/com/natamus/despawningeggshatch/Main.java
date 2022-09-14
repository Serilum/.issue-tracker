/*
 * This is the latest source code of Despawning Eggs Hatch.
 * Minecraft version: 1.19.2, mod version: 2.8.
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
