/*
 * This is the latest source code of Bottle Your Xp.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.bottleyourxp.events;

import com.natamus.bottleyourxp.config.ConfigHandler;
import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.collective_fabric.functions.ExperienceFunctions;
import com.natamus.collective_fabric.functions.ItemFunctions;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ClickEvent {
	public static InteractionResultHolder<ItemStack> onClickBottle(Player player, Level world, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (world.isClientSide) {
			return InteractionResultHolder.pass(itemstack);
		}
		
		if (itemstack.getItem().equals(Items.GLASS_BOTTLE)) {
			if (player.isCrouching()) {
				if (ExperienceFunctions.canConsumeXp(player, ConfigHandler.rawXpConsumedOnCreation.getValue())) {
					if (ConfigHandler.damageOnCreation.getValue() && !player.isCreative()) {
						int damage = ConfigHandler.halfHeartDamageAmount.getValue();
						if (!EntityFunctions.doesEntitySurviveThisDamage(player, damage)) {
							return InteractionResultHolder.pass(itemstack);
						}
					}
					
					ExperienceFunctions.addPlayerXP(player, -ConfigHandler.rawXpConsumedOnCreation.getValue());
					ItemFunctions.shrinkGiveOrDropItemStack(player, hand, itemstack, new ItemStack(Items.EXPERIENCE_BOTTLE, 1));
				}
			}
		}
		
		return InteractionResultHolder.pass(itemstack);
	}
}
