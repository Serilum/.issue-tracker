/*
 * This is the latest source code of Bottle Your Xp.
 * Minecraft version: 1.18.x, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Bottle Your Xp ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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
