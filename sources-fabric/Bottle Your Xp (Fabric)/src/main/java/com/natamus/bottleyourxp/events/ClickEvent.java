/*
 * This is the latest source code of Bottle Your Xp.
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
				if (ExperienceFunctions.canConsumeXp(player, ConfigHandler.rawXpConsumedOnCreation)) {
					if (ConfigHandler.damageOnCreation && !player.isCreative()) {
						int damage = ConfigHandler.halfHeartDamageAmount;
						if (!EntityFunctions.doesEntitySurviveThisDamage(player, damage)) {
							return InteractionResultHolder.pass(itemstack);
						}
					}
					
					ExperienceFunctions.addPlayerXP(player, -ConfigHandler.rawXpConsumedOnCreation);
					ItemFunctions.shrinkGiveOrDropItemStack(player, hand, itemstack, new ItemStack(Items.EXPERIENCE_BOTTLE, 1));
				}
			}
		}
		
		return InteractionResultHolder.pass(itemstack);
	}
}
