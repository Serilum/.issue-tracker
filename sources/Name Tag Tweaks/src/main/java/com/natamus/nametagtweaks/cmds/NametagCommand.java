/*
 * This is the latest source code of Name Tag Tweaks.
 * Minecraft version: 1.16.5, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Name Tag Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.nametagtweaks.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.nametagtweaks.config.ConfigHandler;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class NametagCommand {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
    	dispatcher.register(Commands.literal("nametag")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof ServerPlayerEntity)
			.executes((command) -> {
				StringFunctions.sendMessage(command.getSource(), "Usage: '/nametag <name>' while holding a name tag.", TextFormatting.DARK_GREEN);
				return 1;
			})
			.then(Commands.argument("name", StringArgumentType.word())
			.executes((command) -> {
				PlayerEntity player = command.getSource().getPlayerOrException();
				
				ItemStack nametagstack;
				if (player.getItemInHand(Hand.MAIN_HAND).getItem().equals(Items.NAME_TAG)) {
					nametagstack = player.getItemInHand(Hand.MAIN_HAND);
				}
				else if (player.getItemInHand(Hand.OFF_HAND).getItem().equals(Items.NAME_TAG)) {
					nametagstack = player.getItemInHand(Hand.OFF_HAND);
				}
				else {
					StringFunctions.sendMessage(player, "Usage: '/nametag <name>' while holding a name tag.", TextFormatting.RED);
					return 1;
				}
				
				String name = StringArgumentType.getString(command, "name");
				if (ConfigHandler.GENERAL.nameTagCommandReplaceUnderscoresWithSpaces.get()) {
					name = name.replace("_", " ");
				}
				
				nametagstack.setHoverName(new StringTextComponent(name));
				nametagstack.setRepairCost(0);
				StringFunctions.sendMessage(player, "Set name value to '" + name + "'.", TextFormatting.DARK_GREEN);
				return 1;
			}))
		);
    }
}
