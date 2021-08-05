/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.handoveryouritems.events;

import com.natamus.collective.functions.ItemFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.handoveryouritems.config.HandOverYourItemsConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class HandOverYourItemsHandOverEvent {
	@SubscribeEvent
	public void onPlayerClick(PlayerInteractEvent.EntityInteract e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (!e.getHand().equals(Hand.MAIN_HAND)) {
			return;
		}
		
		Entity target = e.getTarget();
		if (target instanceof PlayerEntity == false) {
			return;
		}
		
		ItemStack stacktogive = e.getItemStack();
		if (stacktogive.isEmpty()) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
		if (HandOverYourItemsConfigHandler.GENERAL.mustCrouchToGiveItem.get()) {
			if (!player.isCrouching()) {
				return;
			}
		}
		
		e.setCanceled(true);
		e.setResult(Result.DENY);
		
		int stacksize = stacktogive.getCount();
		String itemstring = ItemFunctions.itemToReadableString(stacktogive.getItem());
		
		PlayerEntity playertarget = (PlayerEntity)target;
		ItemFunctions.giveOrDropItemStack(playertarget, stacktogive.copy());
		if (HandOverYourItemsConfigHandler.GENERAL.sendItemReceivedMessage.get()) {
			StringFunctions.sendMessage(playertarget, "You have been given " + stacksize + " " + itemstring + " by " + player.getName().getString() + ".", TextFormatting.DARK_GREEN);
		}
		
		stacktogive.setCount(0);
		if (HandOverYourItemsConfigHandler.GENERAL.sendItemGivenMessage.get()) {
			StringFunctions.sendMessage(player, "You have given " + stacksize + " " + itemstring + " to " + target.getName().getString() + ".", TextFormatting.BLUE);
		}
		
		player.inventoryMenu.broadcastChanges();
		playertarget.inventoryMenu.broadcastChanges();
	}
}
