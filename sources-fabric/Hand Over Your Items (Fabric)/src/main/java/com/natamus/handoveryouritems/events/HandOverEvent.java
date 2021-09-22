/*
 * This is the latest source code of Hand Over Your Items.
 * Minecraft version: 1.17.x, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Hand Over Your Items ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.handoveryouritems.events;

import com.natamus.collective_fabric.functions.ItemFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.handoveryouritems.config.ConfigHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class HandOverEvent {
	public static InteractionResult onPlayerClick(Player player, Level world, InteractionHand hand, Entity target, EntityHitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		if (!hand.equals(InteractionHand.MAIN_HAND)) {
			return InteractionResult.PASS;
		}
		
		if (target instanceof Player == false) {
			return InteractionResult.PASS;
		}
		
		ItemStack stacktogive = player.getItemInHand(hand);
		if (stacktogive.isEmpty()) {
			return InteractionResult.PASS;
		}
		
		if (ConfigHandler.mustCrouchToGiveItem.getValue()) {
			if (!player.isCrouching()) {
				return InteractionResult.PASS;
			}
		}
		
		int stacksize = stacktogive.getCount();
		String itemstring = ItemFunctions.itemToReadableString(stacktogive.getItem());
		
		Player playertarget = (Player)target;
		ItemFunctions.giveOrDropItemStack(playertarget, stacktogive.copy());
		if (ConfigHandler.sendItemReceivedMessage.getValue()) {
			StringFunctions.sendMessage(playertarget, "You have been given " + stacksize + " " + itemstring + " by " + player.getName().getString() + ".", ChatFormatting.DARK_GREEN);
		}
		
		stacktogive.setCount(0);
		if (ConfigHandler.sendItemGivenMessage.getValue()) {
			StringFunctions.sendMessage(player, "You have given " + stacksize + " " + itemstring + " to " + target.getName().getString() + ".", ChatFormatting.BLUE);
		}
		
		player.getInventory().setChanged();
		playertarget.getInventory().setChanged();
		
		return InteractionResult.FAIL;
	}
}
