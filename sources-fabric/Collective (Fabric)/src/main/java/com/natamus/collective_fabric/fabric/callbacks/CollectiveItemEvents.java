/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.1.
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CollectiveItemEvents {
	private CollectiveItemEvents() { }
	 
    public static final Event<CollectiveItemEvents.Item_Expire> ON_ITEM_EXPIRE = EventFactory.createArrayBacked(CollectiveItemEvents.Item_Expire.class, callbacks -> (itemEntity, itemStack) -> {
        for (CollectiveItemEvents.Item_Expire callback : callbacks) {
        	callback.onItemExpire(itemEntity, itemStack);
        }
    });
    
    public static final Event<CollectiveItemEvents.Item_Fished> ON_ITEM_FISHED = EventFactory.createArrayBacked(CollectiveItemEvents.Item_Fished.class, callbacks -> (loot, hook) -> {
        for (CollectiveItemEvents.Item_Fished callback : callbacks) {
        	callback.onItemFished(loot, hook);
        }
    });
    
    public static final Event<CollectiveItemEvents.Item_Tossed> ON_ITEM_TOSSED = EventFactory.createArrayBacked(CollectiveItemEvents.Item_Tossed.class, callbacks -> (player, itemStack) -> {
        for (CollectiveItemEvents.Item_Tossed callback : callbacks) {
        	callback.onItemTossed(player, itemStack);
        }
    });

	public static final Event<CollectiveItemEvents.Item_Destroyed> ON_ITEM_DESTROYED = EventFactory.createArrayBacked(CollectiveItemEvents.Item_Destroyed.class, callbacks -> (player, itemStack, hand) -> {
		for (CollectiveItemEvents.Item_Destroyed callback : callbacks) {
			callback.onItemDestroyed(player, itemStack, hand);
		}
	});

	public static final Event<CollectiveItemEvents.Item_Use_Finished> ON_ITEM_USE_FINISHED = EventFactory.createArrayBacked(CollectiveItemEvents.Item_Use_Finished.class, callbacks -> (player, usedItem, newItem, hand) -> {
		for (CollectiveItemEvents.Item_Use_Finished callback : callbacks) {
			ItemStack changedStack = callback.onItemUsedFinished(player, usedItem, newItem, hand);
			if (changedStack != null) {
				return changedStack;
			}
		}
		return null;
	});
    
	@FunctionalInterface
	public interface Item_Expire {
		 void onItemExpire(ItemEntity itemEntity, ItemStack itemStack);
	}
	
	@FunctionalInterface
	public interface Item_Fished {
		 void onItemFished(List<ItemStack> loot, FishingHook hook);
	}
	
	@FunctionalInterface
	public interface Item_Tossed {
		 void onItemTossed(Player player, ItemStack itemStack);
	}

	@FunctionalInterface
	public interface Item_Destroyed {
		void onItemDestroyed(Player player, ItemStack stack, InteractionHand hand);
	}

	@FunctionalInterface
	public interface Item_Use_Finished {
		ItemStack onItemUsedFinished(Player player, ItemStack usedItem, ItemStack newItem, InteractionHand hand);
	}
}
