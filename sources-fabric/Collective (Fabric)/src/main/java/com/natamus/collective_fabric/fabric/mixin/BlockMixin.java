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

package com.natamus.collective_fabric.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.natamus.collective_fabric.fabric.callbacks.CollectiveBlockEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(value = Block.class, priority = 1001)
public class BlockMixin {
	@Inject(method = "setPlacedBy", at = @At(value = "HEAD"), cancellable = true) 
	public void Block_setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack, CallbackInfo ci) {
		if (!CollectiveBlockEvents.BLOCK_PLACE.invoker().onBlockPlace(level, blockPos, blockState, livingEntity, itemStack)) {
			ci.cancel();
		}
	}
	
	@Inject(method = "playerDestroy", at = @At(value = "HEAD"), cancellable = true) 
	public void Block_playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack, CallbackInfo ci) {
		if (!CollectiveBlockEvents.BLOCK_DESTROY.invoker().onBlockDestroy(level, player, blockPos, blockState, blockEntity, itemStack)) {
			ci.cancel();
		}
	}
}
