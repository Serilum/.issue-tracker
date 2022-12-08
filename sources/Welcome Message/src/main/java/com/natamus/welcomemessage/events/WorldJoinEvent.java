/*
 * This is the latest source code of Welcome Message.
 * Minecraft version: 1.19.3, mod version: 1.7.
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

package com.natamus.welcomemessage.events;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.welcomemessage.config.ConfigHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class WorldJoinEvent {
	@SubscribeEvent()
	public void onSpawn(PlayerEvent.PlayerLoggedInEvent e) {
		Player player = e.getEntity();
		Level level = player.level;
		if (level.isClientSide) {
			return;
		}

		if (ConfigHandler.GENERAL.onlyRunOnDedicatedServers.get()) {
			if (!level.getServer().isDedicatedServer()) {
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
