/*
 * This is the latest source code of Save and Load Inventories.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.saveandloadinventories.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.saveandloadinventories.util.Util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.ChatFormatting;

public class CommandListinventories {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("listinventories").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				StringFunctions.sendMessage(source, "Saved inventories: " + Util.getListOfInventories() + ".", ChatFormatting.DARK_GREEN);
				return 1;
			})
		);
    }
}
