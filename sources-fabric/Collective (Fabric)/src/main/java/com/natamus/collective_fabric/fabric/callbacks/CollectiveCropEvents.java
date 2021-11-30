/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 3.9.
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
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CollectiveCropEvents {
	private CollectiveCropEvents() { }
	 
    public static final Event<CollectiveCropEvents.On_Bone_Meal_Apply> ON_BONE_MEAL_APPLY = EventFactory.createArrayBacked(CollectiveCropEvents.On_Bone_Meal_Apply.class, callbacks -> (player, world, pos, state, stack) -> {
        for (CollectiveCropEvents.On_Bone_Meal_Apply callback : callbacks) {
        	if (!callback.onBoneMealApply(player, world, pos, state, stack)) {
        		return false;
        	}
        }
        
        return true;
    });
    
    public static final Event<CollectiveCropEvents.On_General_Bone_Meal_Apply> ON_GENERAL_BONE_MEAL_APPLY = EventFactory.createArrayBacked(CollectiveCropEvents.On_General_Bone_Meal_Apply.class, callbacks -> (world, pos, state, stack) -> {
        for (CollectiveCropEvents.On_General_Bone_Meal_Apply callback : callbacks) {
        	if (!callback.onGeneralBoneMealApply(world, pos, state, stack)) {
        		return false;
        	}
        }
        
        return true;
    });
    
    public static final Event<CollectiveCropEvents.On_Pre_Crop_Grow> PRE_CROP_GROW = EventFactory.createArrayBacked(CollectiveCropEvents.On_Pre_Crop_Grow.class, callbacks -> (world, pos, state) -> {
        for (CollectiveCropEvents.On_Pre_Crop_Grow callback : callbacks) {
        	if (!callback.onPreCropGrow(world, pos, state)) {
        		return false;
        	}
        }
        
        return true;
    });
    
	@FunctionalInterface
	public interface On_Bone_Meal_Apply {
		 boolean onBoneMealApply(Player player, Level world, BlockPos pos, BlockState state, ItemStack stack);
	}
	
	@FunctionalInterface
	public interface On_General_Bone_Meal_Apply {
		 boolean onGeneralBoneMealApply(Level world, BlockPos pos, BlockState state, ItemStack stack);
	}
	
	@FunctionalInterface
	public interface On_Pre_Crop_Grow {
		 boolean onPreCropGrow(Level world, BlockPos pos, BlockState state);
	}
}
