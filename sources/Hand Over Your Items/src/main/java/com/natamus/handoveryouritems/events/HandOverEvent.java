/*
 * This is the latest source code of Hand Over Your Items.
 * Minecraft version: 1.19.3, mod version: 2.0.
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
		if (!e.getHand().equals(InteractionHand.MAIN_HAND)) {
			return;
		}
		
		Entity target = e.getTarget();
		if (!(target instanceof Player)) {
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

		Level level = e.getLevel();
		
		e.setCanceled(true);
		e.setResult(Result.DENY);
		
		int stacksize = stacktogive.getCount();
		String itemstring = ItemFunctions.itemToReadableString(stacktogive.getItem());
		
		Player playertarget = (Player)target;
		ItemFunctions.giveOrDropItemStack(playertarget, stacktogive.copy());
		stacktogive.setCount(0);

		if (!level.isClientSide) {
			if (ConfigHandler.GENERAL.sendItemReceivedMessage.get()) {
				StringFunctions.sendMessage(playertarget, "You have been given " + stacksize + " " + itemstring + " by " + player.getName().getString() + ".", ChatFormatting.DARK_GREEN);
			}

			if (ConfigHandler.GENERAL.sendItemGivenMessage.get()) {
				StringFunctions.sendMessage(player, "You have given " + stacksize + " " + itemstring + " to " + target.getName().getString() + ".", ChatFormatting.BLUE);
			}
		}
		
		player.getInventory().setChanged();
		playertarget.getInventory().setChanged();
	}
}
