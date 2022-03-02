/*
 * This is the latest source code of Welcome Message.
 * Minecraft version: 1.19.x, mod version: 1.0.
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

import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.welcomemessage.config.ConfigHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WorldJoinEvent {
	public static void onSpawn(Level world, Entity entity) {
		if (world.isClientSide) {
			return;
		}

		if (entity instanceof Player == false) {
			return;
		}
		
		Player player = (Player)entity;
		if (ConfigHandler.onlyRunOnDedicatedServers.getValue()) {
			if (!world.getServer().isDedicatedServer()) {
				return;
			}
		}
		
		boolean emptyline = ConfigHandler.sendEmptyLineBeforeFirstMessage.getValue();
		
		if (!ConfigHandler.messageOneText.getValue().isEmpty()) {
			ChatFormatting oneColour = ChatFormatting.getById(ConfigHandler.messageOneColourIndex.getValue());
			if (oneColour == null) {
				System.out.println("[Welcome Message Error] Unable to find text formatting colour for message one with '" + ConfigHandler.messageOneColourIndex.getValue() + "'.");
				return;
			}
			
			StringFunctions.sendMessage(player, ConfigHandler.messageOneText.getValue(), oneColour, emptyline, ConfigHandler.messageOneOptionalURL.getValue().trim());
			emptyline = false;
		}
		
		if (!ConfigHandler.messageTwoText.getValue().isEmpty()) {
			ChatFormatting twoColour = ChatFormatting.getById(ConfigHandler.messageTwoColourIndex.getValue());
			if (twoColour == null) {
				System.out.println("[Welcome Message Error] Unable to find text formatting colour for message two with '" + ConfigHandler.messageTwoColourIndex.getValue() + "'.");
				return;
			}
			
			StringFunctions.sendMessage(player, ConfigHandler.messageTwoText.getValue(), twoColour, emptyline, ConfigHandler.messageTwoOptionalURL.getValue().trim());
			emptyline = false;
		}
		
		if (!ConfigHandler.messageThreeText.getValue().isEmpty()) {
			ChatFormatting threeColour = ChatFormatting.getById(ConfigHandler.messageThreeColourIndex.getValue());
			if (threeColour == null) {
				System.out.println("[Welcome Message Error] Unable to find text formatting colour for message three with '" + ConfigHandler.messageThreeColourIndex.getValue() + "'.");
				return;
			}
			
			StringFunctions.sendMessage(player, ConfigHandler.messageThreeText.getValue(), threeColour, emptyline, ConfigHandler.messageThreeOptionalURL.getValue().trim());
		}
	}
}
