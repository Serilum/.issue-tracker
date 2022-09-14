/*
 * This is the latest source code of Compact Help Command.
 * Minecraft version: 1.19.2, mod version: 1.4.
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

package com.natamus.compacthelpcommand.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import com.natamus.collective.functions.NumberFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.compacthelpcommand.config.ConfigHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandHelp {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("help")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player)
			.executes((command) -> {
				Player player = (Player)command.getSource().getPlayerOrException();
				return processHelpCommands(dispatcher, command, 1, player);
			})
			.then(Commands.argument("page", IntegerArgumentType.integer(0, 1000))
			.executes((command) -> {
				Player player = (Player)command.getSource().getPlayerOrException();
				return processHelpCommands(dispatcher, command, IntegerArgumentType.getInteger(command, "page"), player);
			}))
			.then(Commands.argument("command", StringArgumentType.greedyString())
			.executes((command) -> {
				Player player = (Player)command.getSource().getPlayerOrException();
				String fakecommand = StringArgumentType.getString(command, "command");
				
				if (NumberFunctions.isNumeric(fakecommand)) {
					return processHelpCommands(dispatcher, command, Integer.parseInt(fakecommand), player);
				}
				
				return 1;
			}))
		);
    }
    
    private static Integer processHelpCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandContext<CommandSourceStack> command, Integer page, Player player) {
		List<String> scmds = new ArrayList<String>();
		Map<CommandNode<CommandSourceStack>, String> map = dispatcher.getSmartUsage(dispatcher.getRoot(), command.getSource());
		for(String s : map.values()) {
            String scmd = "/" + s;
            scmds.add(scmd);
        }
		
		Collections.sort(scmds);
		
		int amountperpage = ConfigHandler.GENERAL.amountCommandsPerPage.get();
		int totalcount = scmds.size();
		int totalpages = (int)Math.ceil(totalcount/amountperpage) + 1;
		
		if (page <= 0) {
			page = 1;
		}
		if (page > totalpages) {
			page = totalpages;
		}
		
		ChatFormatting commandcolour = ChatFormatting.getById(ConfigHandler.GENERAL.commandColour.get());
		ChatFormatting subcommandcolour = ChatFormatting.getById(ConfigHandler.GENERAL.subcommandColour.get());

		StringFunctions.sendMessage(player, " ", ChatFormatting.WHITE);
		
		for (int n = 0; n < ((amountperpage * page)); n++) {
			if (n >= ((amountperpage * page) - amountperpage)) {
				if (scmds.size() < n+1) {
					break;
				}
				
				String commandline = scmds.get(n);
				String[] cmdlspl = commandline.split(" ");
				String acmd = cmdlspl[0];
				String csuffix = commandline.replaceAll(acmd, "");
				
				if (ConfigHandler.GENERAL.addVerticalBarSpacing.get()) {
					csuffix = csuffix.replace("|", " | ");
				}
				
				MutableComponent tc = Component.literal("");

				MutableComponent tc0 = Component.literal(acmd);
				tc0.withStyle(commandcolour);
				tc.append(tc0);

				MutableComponent tc1 = Component.literal(csuffix);
				tc1.withStyle(subcommandcolour);
				tc.append(tc1);
				
				player.sendSystemMessage(tc);
			}
		}
		
		StringFunctions.sendMessage(player, " Page " + page + " / " + totalpages + ", /help <page>", ChatFormatting.YELLOW);
		return 1;
    }
}