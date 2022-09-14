/*
 * This is the latest source code of Kelp Fertilizer.
 * Minecraft version: 1.19.2, mod version: 2.2.
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