/*
 * This is the latest source code of Welcome Message.
 * Minecraft version: 1.18.1, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Welcome Message ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.welcomemessage.events;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.welcomemessage.config.ConfigHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class WorldJoinEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSpawn(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Player == false) {
			return;
		}
		
		Player player = (Player)entity;
		if (!e.isCanceled()) {
			if (ConfigHandler.GENERAL.onlyRunOnDedicatedServers.get()) {
				if (!world.getServer().isDedicatedServer()) {
					return;
				}
			}
			
			boolean emptyline = ConfigHandler.GENERAL.sendEmptyLineBeforeFirstMessage.get();
			
			if (!ConfigHandler.GENERAL.messageOneText.get().isEmpty()) {
				ChatFormatting oneColour = ChatFormatting.getById(ConfigHandler.GENERAL.messageOneColourIndex.get());
				if (oneColour == null) {
					System.out.println("[Welcome Message Error] Unable to find text formatting colour for message one with '" + ConfigHandler.GENERAL.messageOneColourIndex.get() + "'.");
					return;
				}
				
				StringFunctions.sendMessage(player, ConfigHandler.GENERAL.messageOneText.get(), oneColour, emptyline, ConfigHandler.GENERAL.messageOneOptionalURL.get().trim());
				emptyline = false;
			}
			
			if (!ConfigHandler.GENERAL.messageTwoText.get().isEmpty()) {
				ChatFormatting twoColour = ChatFormatting.getById(ConfigHandler.GENERAL.messageTwoColourIndex.get());
				if (twoColour == null) {
					System.out.println("[Welcome Message Error] Unable to find text formatting colour for message two with '" + ConfigHandler.GENERAL.messageTwoColourIndex.get() + "'.");
					return;
				}
				
				StringFunctions.sendMessage(player, ConfigHandler.GENERAL.messageTwoText.get(), twoColour, emptyline, ConfigHandler.GENERAL.messageTwoOptionalURL.get().trim());
				emptyline = false;
			}
			
			if (!ConfigHandler.GENERAL.messageThreeText.get().isEmpty()) {
				ChatFormatting threeColour = ChatFormatting.getById(ConfigHandler.GENERAL.messageThreeColourIndex.get());
				if (threeColour == null) {
					System.out.println("[Welcome Message Error] Unable to find text formatting colour for message three with '" + ConfigHandler.GENERAL.messageThreeColourIndex.get() + "'.");
					return;
				}
				
				StringFunctions.sendMessage(player, ConfigHandler.GENERAL.messageThreeText.get(), threeColour, emptyline, ConfigHandler.GENERAL.messageThreeOptionalURL.get().trim());
			}
		}
	}
}
