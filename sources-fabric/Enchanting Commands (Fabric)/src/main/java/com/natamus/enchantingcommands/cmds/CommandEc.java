/*
 * This is the latest source code of Enchanting Commands.
 * Minecraft version: 1.19.2, mod version: 2.6.
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

package com.natamus.enchantingcommands.cmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.enchantingcommands.config.ConfigHandler;
import com.natamus.enchantingcommands.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ItemEnchantmentArgument;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class CommandEc {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal(ConfigHandler.enchantCommandString)
			.requires((iCommandSender) -> iCommandSender.hasPermission(2))
			.executes((command) -> {
				sendUsage(command.getSource());
				return 1;
			})
			.then(Commands.literal("list")
			.executes((command) -> {
				CommandSourceStack source = command.getSource();

				String joined = String.join(", ", Util.getEnchantmentKeys());
				StringFunctions.sendMessage(source, "--- Enchanting Commands List ---", ChatFormatting.DARK_GREEN, true);
				StringFunctions.sendMessage(source, " " + joined, ChatFormatting.DARK_GREEN);
				return 1;
			}))
			.then(Commands.literal("enchant")
			.then(Commands.argument("enchantment", ItemEnchantmentArgument.enchantment())
			.then(Commands.argument("level", IntegerArgumentType.integer(0, 127))
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				Entity entity = source.getEntity();
				if (!(entity instanceof ServerPlayer)) {
					StringFunctions.sendMessage(source, "This command can only be executed as a player.", ChatFormatting.RED);
					return 1;
				}

				Player player = (ServerPlayer)entity;
				ItemStack held = player.getMainHandItem();

				Enchantment enchantment = ItemEnchantmentArgument.getEnchantment(command, "enchantment");
				int level = IntegerArgumentType.getInteger(command, "level");

				if (!player.hasItemInSlot(EquipmentSlot.MAINHAND)) {
					StringFunctions.sendMessage(player, "You do not have an enchantable item in your main hand.", ChatFormatting.RED);
					return 0;
				}

				ItemStack temp = new ItemStack(Item.byId(1));
				temp.enchant(enchantment, level);
				String estringtemp = temp.getEnchantmentTags().get(0).toString().split("id:")[1];

				boolean removed = false;
				for (Tag nbt : held.getEnchantmentTags()) {
					if (estringtemp.equals(nbt.toString().split("id:")[1])) {
						held.getEnchantmentTags().remove(nbt);
						removed = true;
						break;
					}
				}

				String enchantmentname = enchantment.getDescriptionId().replace("enchantment.", "");
				if (level != 0) {
					held.enchant(enchantment, level);
					StringFunctions.sendMessage(player, "The enchantment '" + enchantmentname + "' has been added to the item with a level of " + level + ".", ChatFormatting.DARK_GREEN);
				}
				else if (removed) {
					StringFunctions.sendMessage(player, "The enchantment '" + enchantmentname + "' has been removed from the item.", ChatFormatting.DARK_GREEN);
				}
				else {
					StringFunctions.sendMessage(player, "The enchantment '" + enchantmentname + "' does not exist on the item.", ChatFormatting.RED);
				}
				return 1;
			}))))
		);
    }

	public static void sendUsage(CommandSourceStack source) {
		StringFunctions.sendMessage(source, "--- Enchanting Commands Usage ---", ChatFormatting.DARK_GREEN, true);
		StringFunctions.sendMessage(source, " /" + ConfigHandler.enchantCommandString + " list", ChatFormatting.DARK_GREEN);
		StringFunctions.sendMessage(source, " /" + ConfigHandler.enchantCommandString + " enchant <enchant> <lvl>", ChatFormatting.DARK_GREEN);
	}

	public static void sendUsage(Player player) {
		StringFunctions.sendMessage(player, "--- Enchanting Commands Usage ---", ChatFormatting.DARK_GREEN, true);
		StringFunctions.sendMessage(player, " /" + ConfigHandler.enchantCommandString + " list", ChatFormatting.DARK_GREEN);
		StringFunctions.sendMessage(player, " /" + ConfigHandler.enchantCommandString + " enchant <enchant> <lvl>", ChatFormatting.DARK_GREEN);
	}
}