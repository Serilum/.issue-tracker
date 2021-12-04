/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 3.13.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.fabric.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.ItemStack;

public class CollectiveFurnaceEvents {
	private CollectiveFurnaceEvents() { }
	 
    public static final Event<CollectiveFurnaceEvents.Calculate_Furnace_Burn_Time> CALCULATE_FURNACE_BURN_TIME = EventFactory.createArrayBacked(CollectiveFurnaceEvents.Calculate_Furnace_Burn_Time.class, callbacks -> (itemStack, burntime) -> {
        for (CollectiveFurnaceEvents.Calculate_Furnace_Burn_Time callback : callbacks) {
        	int newburntime = callback.getFurnaceBurnTime(itemStack, burntime);
        	if (burntime != newburntime) {
        		return newburntime;
        	}
        }
        
        return burntime;
    });
    
	@FunctionalInterface
	public interface Calculate_Furnace_Burn_Time {
		 int getFurnaceBurnTime(ItemStack itemStack, int burntime);
	}
}
