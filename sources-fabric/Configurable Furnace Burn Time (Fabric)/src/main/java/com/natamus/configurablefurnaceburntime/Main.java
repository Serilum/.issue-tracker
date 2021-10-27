/*
 * This is the latest source code of Configurable Furnace Burn Time.
 * Minecraft version: 1.17.x, mod version: 1.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Configurable Furnace Burn Time ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.configurablefurnaceburntime;

import com.natamus.collective_fabric.check.RegisterMod;
import com.natamus.collective_fabric.fabric.callbacks.CollectiveFurnaceEvents;
import com.natamus.configurablefurnaceburntime.config.ConfigHandler;
import com.natamus.configurablefurnaceburntime.events.FurnaceBurnEvent;
import com.natamus.configurablefurnaceburntime.util.Reference;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.ItemStack;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() { 
		ConfigHandler.setup();

		registerEvents();
		
		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void registerEvents() {
		CollectiveFurnaceEvents.CALCULATE_FURNACE_BURN_TIME.register((ItemStack itemStack, int burntime) -> {
			return FurnaceBurnEvent.furnaceBurnTimeEvent(itemStack, burntime);
		});
	}
}
