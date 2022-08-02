/*
 * This is the latest source code of Vote Command.
 * Minecraft version: 1.19.1, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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