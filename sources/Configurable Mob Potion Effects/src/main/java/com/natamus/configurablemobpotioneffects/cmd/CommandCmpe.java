/*
 * This is the latest source code of Configurable Mob Potion Effects.
 * Minecraft version: 1.19.3, mod version: 2.1.
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

package com.natamus.configurablemobpotioneffects.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.configurablemobpotioneffects.util.Util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.ChatFormatting;

public class CommandCmpe {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("cmpe").requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				StringFunctions.sendMessage(source, "Usage:", ChatFormatting.DARK_GREEN);
				StringFunctions.sendMessage(source, " /cmpe reload", ChatFormatting.YELLOW);
				StringFunctions.sendMessage(source, "  Reloads the config files located in ./config/configurablemobpotioneffects/", ChatFormatting.DARK_GRAY);
				return 1;
			})
			.then(Commands.literal("reload")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
		    	try {
					Util.loadMobConfigFile();
				} catch (Exception ex) {
					StringFunctions.sendMessage(source, "Something went wrong while reloading the config file.", ChatFormatting.RED);
					ex.printStackTrace();
					return 0;
				}
		    	
		    	StringFunctions.sendMessage(source, "Successfully loaded the mob potion effect config files.", ChatFormatting.DARK_GREEN);
				return 1;
			}))
		);
    }
}
