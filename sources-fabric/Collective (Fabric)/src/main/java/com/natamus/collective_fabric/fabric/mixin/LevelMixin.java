/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.56.
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

package com.natamus.collective_fabric.fabric.mixin;

import java.util.EnumSet;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

@Mixin(value = Level.class, priority = 1001)
public class LevelMixin {
	@Inject(method = "updateNeighborsAt", at = @At(value = "HEAD"), cancellable = true) 
	public void Level_updateNeighborsAt(BlockPos blockPos, Block block, CallbackInfo ci) {
		Level level = (Level)(Object)this;
		if (!CollectiveBlockEvents.NEIGHBOUR_NOTIFY.invoker().onNeighbourNotify(level, blockPos, level.getBlockState(blockPos), EnumSet.allOf(Direction.class), false)) {
			ci.cancel();
		}
	}
	
	@Inject(method = "updateNeighborsAtExceptFromFacing", at = @At(value = "HEAD"), cancellable = true) 
	public void Level_updateNeighborsAtExceptFromFacing(BlockPos blockPos, Block block, Direction direction, CallbackInfo ci) {
		Level level = (Level)(Object)this;
		EnumSet<Direction> directions = EnumSet.allOf(Direction.class);
		directions.remove(direction);
		if (!CollectiveBlockEvents.NEIGHBOUR_NOTIFY.invoker().onNeighbourNotify(level, blockPos, level.getBlockState(blockPos), directions, false)) {
			ci.cancel();
		}
	}
}
