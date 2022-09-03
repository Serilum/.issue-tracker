/*
 * This is the latest source code of Welcome Message.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
