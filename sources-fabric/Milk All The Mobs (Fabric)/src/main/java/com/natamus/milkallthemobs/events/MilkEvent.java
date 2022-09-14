/*
 * This is the latest source code of Milk All The Mobs.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.milkallthemobs.events;

import com.natamus.collective_fabric.functions.EntityFunctions;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class MilkEvent {
	public static InteractionResult onEntityInteract(Player player, Level world, InteractionHand hand, Entity target, EntityHitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.getItem().equals(Items.BUCKET)) {
			if (EntityFunctions.isMilkable(target)) {
				player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
				itemstack.shrink(1);
				
				if (itemstack.isEmpty()) {
					player.setItemInHand(hand, new ItemStack(Items.MILK_BUCKET));
				}
				else if (!player.getInventory().add(new ItemStack(Items.MILK_BUCKET))) {
					player.drop(new ItemStack(Items.MILK_BUCKET), false);
				}
				
				return InteractionResult.SUCCESS;
			}
		}
		
		return InteractionResult.PASS;
	}
}
