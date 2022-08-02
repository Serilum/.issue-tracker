/*
 * This is the latest source code of Kelp Fertilizer.
 * Minecraft version: 1.19.1, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.kelpfertilizer.events;

import com.natamus.collective_fabric.functions.CropFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class KelpEvent {
	public static boolean onKelpUse(Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
		if (world.isClientSide) {
			return true;
		}
		
		ItemStack itemstack = player.getItemInHand(hand);
		if (!itemstack.getItem().equals(Items.KELP)) {
			return true;
		}
		
		if (CropFunctions.applyBonemeal(itemstack, world, pos, player)) {
			world.levelEvent(2005, pos, 0);
			player.swing(hand);
			return true;
		}
		
		return true;
	}
}