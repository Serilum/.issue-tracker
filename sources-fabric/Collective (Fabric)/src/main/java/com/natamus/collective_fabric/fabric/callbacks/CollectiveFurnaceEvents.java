/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.49.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
