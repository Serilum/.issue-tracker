/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.16.5, mod version: 1.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Silence Mobs ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.silencemobs.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.silencemobs.config.ConfigHandler;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandSt {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("st")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity && (!ConfigHandler.GENERAL.onlyAllowCommandWhenCheatsEnabled.get() || iCommandSender.hasPermissionLevel(2)))
			.executes((command) -> {
				processSilencestick(command);
				return 1;
			})
		);
    	dispatcher.register(Commands.literal("silencestick")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof PlayerEntity && (!ConfigHandler.GENERAL.onlyAllowCommandWhenCheatsEnabled.get() || iCommandSender.hasPermissionLevel(2)))
			.executes((command) -> {
				processSilencestick(command);
				return 1;
			})
		);
    }
    
    public static void processSilencestick(CommandContext<CommandSource> command) throws CommandSyntaxException {
    	CommandSource source = command.getSource();
    	PlayerEntity player = (PlayerEntity)source.asPlayer();
		
		if (ConfigHandler.GENERAL.mustHoldStick.get()) {
			ItemStack held = player.getHeldItem(Hand.MAIN_HAND);
			if (!held.isItemEqual(new ItemStack(Items.STICK, 1))) {
				StringFunctions.sendMessage(player, "You must hold a stick in your main hand to transform it into a silence-stick.", TextFormatting.DARK_RED);
				return;
			}
			player.getHeldItemMainhand().shrink(1);
		}
		
		ItemStack silencestick = new ItemStack(Items.STICK, 1);
		silencestick.setDisplayName(new StringTextComponent(TextFormatting.GOLD + "The Silence Stick"));
		player.addItemStackToInventory(silencestick);
		StringFunctions.sendMessage(player, "You have been given The Silence Stick! Handle with care.", TextFormatting.DARK_GREEN);
		return;			
    }
}