/*
 * This is the latest source code of Entity Information.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Entity Information ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.entityinformation.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective_fabric.functions.StringFunctions;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class CommandIst {
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