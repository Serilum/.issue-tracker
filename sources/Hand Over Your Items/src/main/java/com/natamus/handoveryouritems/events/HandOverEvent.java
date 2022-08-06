/*
 * This is the latest source code of Hand Over Your Items.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.handoveryouritems.events;

import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.handoveryouritems.config.ConfigHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class HandOverEvent {
	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		if (!e.getHand().equals(InteractionHand.MAIN_HAND)) {
			return;
		}
		
		Entity target = e.getTarget();
		if (target instanceof Player == false) {
			return;
		}
		
		ItemStack stacktogive = e.getItemStack();
		if (stacktogive.isEmpty()) {
			return;
		}
		
		Player player = e.getEntity();
		if (ConfigHandler.GENERAL.mustCrouchToGiveItem.get()) {
			if (!player.isCrouching()) {
				return;
			}
		}
		
		e.setCanceled(true);
		e.setResult(Result.DENY);
		
		int stacksize = stacktogive.getCount();
		String itemstring = ItemFunctions.itemToReadableString(stacktogive.getItem());
		
		Player playertarget = (Player)target;
		ItemFunctions.giveOrDropItemStack(playertarget, stacktogive.copy());
		if (ConfigHandler.GENERAL.sendItemReceivedMessage.get()) {
			StringFunctions.sendMessage(playertarget, "You have been given " + stacksize + " " + itemstring + " by " + player.getName().getString() + ".", ChatFormatting.DARK_GREEN);
		}
		
		stacktogive.setCount(0);
		if (ConfigHandler.GENERAL.sendItemGivenMessage.get()) {
			StringFunctions.sendMessage(player, "You have given " + stacksize + " " + itemstring + " to " + target.getName().getString() + ".", ChatFormatting.BLUE);
		}
		
		player.getInventory().setChanged();
		playertarget.getInventory().setChanged();
	}
}
