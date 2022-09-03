/*
 * This is the latest source code of Vote Command.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.votecommand.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.votecommand.config.ConfigHandler;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.ChatFormatting;

public class CommandVote {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("vote")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player)
			.executes((command) -> {
				Player player = (Player)command.getSource().getPlayerOrException();
				
				StringFunctions.sendMessage(player, ConfigHandler.voteCommandMessage.getValue(), ChatFormatting.DARK_GREEN);
				return 1;
			})
		);
    }
}