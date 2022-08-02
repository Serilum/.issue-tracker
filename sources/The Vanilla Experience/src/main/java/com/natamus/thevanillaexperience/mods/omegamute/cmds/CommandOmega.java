/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.omegamute.cmds;

import java.util.HashMap;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.omegamute.events.OmegaMuteMuteEvent;
import com.natamus.thevanillaexperience.mods.omegamute.util.OmegaMuteUtil;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.ChatFormatting;

public class CommandOmega {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("omegamute")
			.then(Commands.literal("reload")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				StringFunctions.sendMessage(source, "Reloading the omega mute soundmap file now.", ChatFormatting.DARK_GREEN);
				try {
					if (OmegaMuteUtil.loadSoundFile()) {
						StringFunctions.sendMessage(source, "New soundmap changes successfully loaded.", ChatFormatting.DARK_GREEN);
					}
					else {
						StringFunctions.sendMessage(source, "No soundmap found, a new one has been generated.", ChatFormatting.DARK_GREEN);
					}
				} catch (Exception ex) {
					StringFunctions.sendMessage(source, "Something went wrong while loading the soundmap file.", ChatFormatting.RED);
				}
				return 1;
			}))
			.then(Commands.literal("query")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				HashMap<String, Integer> mutedsounds = OmegaMuteUtil.getMutedSounds();
				if (mutedsounds.size() > 0) {
					String combined = "";
					for (String soundname : mutedsounds.keySet()) {
						if (combined != "") {
							combined += ", ";
						}
						Integer mutedvalue = mutedsounds.get(soundname);
						if (mutedvalue > 0) {
							combined += soundname + "(" + mutedvalue + ")";
						}
						else {
							combined += soundname;
						}
					}
					
					StringFunctions.sendMessage(source, "The following sound events are currently muted:", ChatFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, ChatFormatting.YELLOW);
				}
				else {
					StringFunctions.sendMessage(source, "There are currently no sound events muted.", ChatFormatting.DARK_GREEN);
				}
				
				return 1;
			}))
			.then(Commands.literal("listen").requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player)
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				Player player = (Player)source.getPlayerOrException();
				if (OmegaMuteMuteEvent.listeningplayers.contains(player)) {
					OmegaMuteMuteEvent.listeningplayers.remove(player);
					StringFunctions.sendMessage(player, "You have stopped listening to the active sounds. To toggle it on use '/omegamute listen' again.", ChatFormatting.DARK_GREEN);
				}
				else {
					OmegaMuteMuteEvent.listeningplayers.add(player);
					StringFunctions.sendMessage(player, "You are now listening to the active sounds. To toggle it off use '/omegamute listen' again.", ChatFormatting.DARK_GREEN);
				}
				
				return 1;
			}))
			.then(Commands.literal("mute")
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				String wildcard = StringArgumentType.getString(command, "string-contains");
				List<String> muted = OmegaMuteUtil.muteWildcard(wildcard, 0);
				if (muted.size() > 0) {
					String combined = String.join(", ", muted);
					StringFunctions.sendMessage(source, "By using the wildcard string '" + wildcard + "', the following " + muted.size() + " sound events have been muted:", ChatFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, ChatFormatting.YELLOW);
					StringFunctions.sendMessage(source, "The soundmap file has been updated.", ChatFormatting.DARK_GREEN);
				}
				else {
					StringFunctions.sendMessage(source, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", ChatFormatting.RED);
				}
				
				return 1;
			})))
			.then(Commands.literal("cull")
			.then(Commands.argument("cull-time", IntegerArgumentType.integer(0, 3600))
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				int culltime = IntegerArgumentType.getInteger(command, "cull-time");
				String wildcard = StringArgumentType.getString(command, "string-contains");
				
				List<String> muted = OmegaMuteUtil.muteWildcard(wildcard, culltime);
				if (muted.size() > 0) {
					String combined = String.join(", ", muted);
					StringFunctions.sendMessage(source, "By using the wildcard string '" + wildcard + "', the following " + muted.size() + " sound events have been muted with a cull-time of " + culltime + ":", ChatFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, ChatFormatting.YELLOW);
					StringFunctions.sendMessage(source, "The soundmap file has been updated.", ChatFormatting.DARK_GREEN);
				}
				else {
					StringFunctions.sendMessage(source, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", ChatFormatting.RED);
				}
				
				return 1;
			}))))
			.then(Commands.literal("unmute")
			.then(Commands.argument("string-contains", StringArgumentType.word())
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				String wildcard = StringArgumentType.getString(command, "string-contains");
				List<String> unmuted = OmegaMuteUtil.unmuteWildcard(wildcard);
				if (unmuted.size() > 0) {
					String combined = String.join(", ", unmuted);
					StringFunctions.sendMessage(source, "By using the wildcard string '" + wildcard + "', the following " + unmuted.size() + " sound events have been unmuted:", ChatFormatting.DARK_GREEN);
					StringFunctions.sendMessage(source, combined, ChatFormatting.YELLOW);
					StringFunctions.sendMessage(source, "The soundmap file has been updated.", ChatFormatting.DARK_GREEN);}
				else {
					StringFunctions.sendMessage(source, "No sound events were found by using the wildcard string '" + wildcard + "', try a different query.", ChatFormatting.RED);
				}
				
				return 1;
			})))
		);
    }
}