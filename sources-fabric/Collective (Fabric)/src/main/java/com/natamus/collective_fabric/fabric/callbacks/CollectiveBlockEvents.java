/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.55.
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
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.BlockHitResult;

import java.util.EnumSet;

public class CollectiveBlockEvents {
	private CollectiveBlockEvents() { }
	 
    public static final Event<CollectiveBlockEvents.On_Block_Place> BLOCK_PLACE = EventFactory.createArrayBacked(CollectiveBlockEvents.On_Block_Place.class, callbacks -> (level, blockPos, blockState, livingEntity, itemStack) -> {
        for (CollectiveBlockEvents.On_Block_Place callback : callbacks) {
        	if (!callback.onBlockPlace(level, blockPos, blockState, livingEntity, itemStack)) {
        		return false;
        	}
        }
        
        return true;
    });
    
    public static final Event<CollectiveBlockEvents.On_Block_Destroy> BLOCK_DESTROY = EventFactory.createArrayBacked(CollectiveBlockEvents.On_Block_Destroy.class, callbacks -> (level, player, blockPos, blockState, blockEntity, itemStack) -> {
        for (CollectiveBlockEvents.On_Block_Destroy callback : callbacks) {
        	if (!callback.onBlockDestroy(level, player, blockPos, blockState, blockEntity, itemStack)) {
        		return false;
        	}
        }
        
        return true;
    });
    
    public static final Event<CollectiveBlockEvents.On_Neighbour_Notify> NEIGHBOUR_NOTIFY = EventFactory.createArrayBacked(CollectiveBlockEvents.On_Neighbour_Notify.class, callbacks -> (world, pos, state, notifiedSides, forceRedstoneUpdate) -> {
        for (CollectiveBlockEvents.On_Neighbour_Notify callback : callbacks) {
        	if (!callback.onNeighbourNotify(world, pos, state, notifiedSides, forceRedstoneUpdate)) {
        		return false;
        	}
        }
        
        return true;
    });
	
    public static final Event<CollectiveBlockEvents.Possible_Portal_Spawn> ON_NETHER_PORTAL_SPAWN = EventFactory.createArrayBacked(CollectiveBlockEvents.Possible_Portal_Spawn.class, callbacks -> (world, pos, shape) -> {
        for (CollectiveBlockEvents.Possible_Portal_Spawn callback : callbacks) {
        	callback.onPossiblePortal(world, pos, shape);
        }
    });
    
    public static final Event<CollectiveBlockEvents.Block_Right_Click> BLOCK_RIGHT_CLICK = EventFactory.createArrayBacked(CollectiveBlockEvents.Block_Right_Click.class, callbacks -> (world, player, hand, pos, hitVec) -> {
        for (CollectiveBlockEvents.Block_Right_Click callback : callbacks) {
        	if (!callback.onBlockRightClick(world, player, hand, pos, hitVec)) {
        		return false;
        	}
        }
        
        return true;
    });

	public static final Event<CollectiveBlockEvents.Block_Left_Click> BLOCK_LEFT_CLICK = EventFactory.createArrayBacked(CollectiveBlockEvents.Block_Left_Click.class, callbacks -> (world, player, pos, direction) -> {
		for (CollectiveBlockEvents.Block_Left_Click callback : callbacks) {
			if (!callback.onBlockLeftClick(world, player, pos, direction)) {
				return false;
			}
		}

		return true;
	});
    
	@FunctionalInterface
	public interface On_Block_Place {
		 boolean onBlockPlace(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack);
	}
	
	@FunctionalInterface
	public interface On_Block_Destroy {
		 boolean onBlockDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack);
	}
	
	@FunctionalInterface
	public interface On_Neighbour_Notify {
		 boolean onNeighbourNotify(Level world, BlockPos pos, BlockState state, EnumSet<Direction> notifiedSides, boolean forceRedstoneUpdate);
	}
    
	@FunctionalInterface
	public interface Possible_Portal_Spawn {
		 void onPossiblePortal(Level world, BlockPos pos, PortalShape shape);
	}
	
	@FunctionalInterface
	public interface Block_Right_Click {
		 boolean onBlockRightClick(Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec);
	}

	@FunctionalInterface
	public interface Block_Left_Click {
		boolean onBlockLeftClick(Level world, Player player, BlockPos pos, Direction direction);
	}
}
