/*
 * This is the latest source code of Welcome Message.
 * Minecraft version: 1.19.2, mod version: 1.3.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.welcomemessage.events;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.welcomemessage.config.ConfigHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class WorldJoinEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSpawn(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
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
