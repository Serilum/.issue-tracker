/*
 * This is the latest source code of Entity Information.
 * Minecraft version: 1.16.5, mod version: 1.6.
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
import com.natamus.collective.functions.StringFunctions;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandSt {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("ist")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity && iCommandSender.hasPermission(2))
			.executes((command) -> {
				processInformationstick(command);
				return 1;
			})
		);
    	dispatcher.register(Commands.literal("informationstick")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity && iCommandSender.hasPermission(2))
			.executes((command) -> {
				processInformationstick(command);
				return 1;
			})
		);
    }
    
    public static void processInformationstick(CommandContext<CommandSource> command) throws CommandSyntaxException {
    	CommandSource source = command.getSource();
		PlayerEntity player = (PlayerEntity)source.getPlayerOrException();
		
		ItemStack informationstick = new ItemStack(Items.STICK, 1);
		informationstick.setHoverName(new StringTextComponent(TextFormatting.BLUE + "The Information Stick"));
		player.addItem(informationstick);
		StringFunctions.sendMessage(player, "You have been given The Information Stick!", TextFormatting.BLUE);
		return;			
    }
}