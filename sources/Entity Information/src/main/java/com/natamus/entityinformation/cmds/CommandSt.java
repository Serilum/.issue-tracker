/*
 * This is the latest source code of Entity Information.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.entityinformation.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.StringFunctions;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class CommandSt {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("ist")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player && iCommandSender.hasPermission(2))
			.executes((command) -> {
				processInformationstick(command);
				return 1;
			})
		);
    	dispatcher.register(Commands.literal("informationstick")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof Player && iCommandSender.hasPermission(2))
			.executes((command) -> {
				processInformationstick(command);
				return 1;
			})
		);
    }
    
    public static void processInformationstick(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
    	CommandSourceStack source = command.getSource();
		Player player = (Player)source.getPlayerOrException();
		
		ItemStack informationstick = new ItemStack(Items.STICK, 1);
		informationstick.setHoverName(Component.literal(ChatFormatting.BLUE + "The Information Stick"));
		player.addItem(informationstick);
		StringFunctions.sendMessage(player, "You have been given The Information Stick!", ChatFormatting.BLUE);
		return;			
    }
}