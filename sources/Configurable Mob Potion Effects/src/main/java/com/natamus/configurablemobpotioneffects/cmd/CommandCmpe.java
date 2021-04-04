/*
 * This is the latest source code of Configurable Mob Potion Effects.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Configurable Mob Potion Effects ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.configurablemobpotioneffects.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.configurablemobpotioneffects.util.Util;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextFormatting;

public class CommandCmpe {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("cmpe").requires((iCommandSender) -> iCommandSender.hasPermissionLevel(2))
			.executes((command) -> {
				CommandSource source = command.getSource();
				
				StringFunctions.sendMessage(source, "Usage:", TextFormatting.DARK_GREEN);
				StringFunctions.sendMessage(source, " /cmpe reload", TextFormatting.YELLOW);
				StringFunctions.sendMessage(source, "  Reloads the config files located in ./config/configurablemobpotioneffects/", TextFormatting.DARK_GRAY);
				return 1;
			})
			.then(Commands.literal("reload")
			.executes((command) -> {
				CommandSource source = command.getSource();
				
		    	try {
					Util.loadMobConfigFile();
				} catch (Exception ex) {
					StringFunctions.sendMessage(source, "Something went wrong while reloading the config file.", TextFormatting.RED);
					ex.printStackTrace();
					return 0;
				}
		    	
		    	StringFunctions.sendMessage(source, "Successfully loaded the mob potion effect config files.", TextFormatting.DARK_GREEN);
				return 1;
			}))
		);
    }
}
