/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.3, mod version: 5.43.
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
