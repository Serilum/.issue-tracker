/*
 * This is the latest source code of Hand Over Your Items.
 * Minecraft version: 1.18.0, mod version: 1.3.
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
		Level world = e.getWorld();
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
		
		Player player = e.getPlayer();
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
