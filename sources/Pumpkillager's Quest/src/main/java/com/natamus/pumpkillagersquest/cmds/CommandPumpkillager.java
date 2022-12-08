/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.3, mod version: 2.3.
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

package com.natamus.pumpkillagersquest.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.natamus.collective.functions.MessageFunctions;
import com.natamus.pumpkillagersquest.util.Reference;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandPumpkillager {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("pumpkillager").requires((iCommandSender) -> {
			return iCommandSender.hasPermission(2);
		}).then(Commands.literal("reset").then(Commands.argument("targets", EntityArgument.entities()).executes((command) -> {
			CommandSourceStack source = command.getSource();
			for (Entity entity : EntityArgument.getEntities(command, "targets")) {
				List<String> tags = entity.getTags().stream().toList();

				int deletedCount = 0;
				for (String tag : tags) {
					if (tag.contains(Reference.MOD_ID)) {
						entity.getTags().remove(tag);
						deletedCount += 1;
					}
				}
				MessageFunctions.sendMessage(source, "Successfully deleted " + deletedCount + " pumpkillager tags for " + entity.getName().getString() + ".", ChatFormatting.DARK_GREEN);
			}
			return 1;
		}))));
    }
}
