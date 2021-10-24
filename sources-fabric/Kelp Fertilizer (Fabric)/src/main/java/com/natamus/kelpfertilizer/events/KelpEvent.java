/*
 * This is the latest source code of Kelp Fertilizer.
 * Minecraft version: 1.17.x, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Kelp Fertilizer ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.kelpfertilizer.events;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.collective_fabric.functions.CropFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class KelpEvent {
	public static InteractionResult onKelpUse(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		ItemStack itemstack = player.getItemInHand(hand);
		if (!itemstack.getItem().equals(Items.KELP)) {
			return InteractionResult.PASS;
		}
		
		BlockPos cpos = BlockPosFunctions.getBlockPosFromHitResult(hitResult).below();
		if (CropFunctions.applyBonemeal(itemstack, world, cpos, player)) {
			world.levelEvent(2005, cpos, 0);
			player.swing(hand);
			return InteractionResult.SUCCESS;
		}
		
		return InteractionResult.PASS;
	}
}