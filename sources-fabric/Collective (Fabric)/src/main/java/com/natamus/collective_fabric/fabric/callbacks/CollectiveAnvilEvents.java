/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 4.12.
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
