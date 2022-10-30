/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.13.
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
