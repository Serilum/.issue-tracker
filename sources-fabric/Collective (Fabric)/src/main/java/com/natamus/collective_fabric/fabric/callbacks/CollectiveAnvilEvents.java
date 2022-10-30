/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.14.
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

import com.natamus.collective_fabric.objects.Triplet;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

public class CollectiveAnvilEvents {
	private CollectiveAnvilEvents() { }
	 
    public static final Event<CollectiveAnvilEvents.Anvil_Change> ANVIL_CHANGE = EventFactory.createArrayBacked(CollectiveAnvilEvents.Anvil_Change.class, callbacks -> (anvilmenu, left, right, output, itemName, baseCost, player) -> {
        for (CollectiveAnvilEvents.Anvil_Change callback : callbacks) {
        	Triplet<Integer, Integer, ItemStack> triple = callback.onAnvilChange(anvilmenu, left, right, output, itemName, baseCost, player);
        	if (triple != null) {
        		return triple;
        	}
        }
        
        return null;
    });
    
	@FunctionalInterface
	public interface Anvil_Change {
		 Triplet<Integer, Integer, ItemStack> onAnvilChange(AnvilMenu anvilmenu, ItemStack left, ItemStack right, ItemStack output, String itemName, int baseCost, Player player);
	}
}
