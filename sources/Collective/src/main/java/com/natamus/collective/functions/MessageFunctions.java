/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.65.
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

package com.natamus.collective.functions;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class MessageFunctions {
    public static void sendMessage(CommandSourceStack source, MutableComponent message) {
        sendMessage(source, message, false);
    }
    public static void sendMessage(Player player, MutableComponent message) {
        sendMessage(player, message, false);
    }

    public static void sendMessage(CommandSourceStack source, String m, ChatFormatting colour) {
        sendMessage(source, m, colour, false);
    }
    public static void sendMessage(Player player, String m, ChatFormatting colour) {
        sendMessage(player, m, colour, false);
    }

    public static void sendMessage(CommandSourceStack source, String m, ChatFormatting colour, boolean emptyline) {
        sendMessage(source, m, colour, emptyline, "");
    }
    public static void sendMessage(Player player, String m, ChatFormatting colour, boolean emptyline) {
        sendMessage(player, m, colour, emptyline, "");
    }

    public static void sendMessage(CommandSourceStack source, String m, ChatFormatting colour, String url) {
        sendMessage(source, m, colour, false, url);
    }
    public static void sendMessage(Player player, String m, ChatFormatting colour, String url) {
        sendMessage(player, m, colour, false, url);
    }

    public static void sendMessage(CommandSourceStack source, String m, ChatFormatting colour, boolean emptyline, String url) {
        if (m.isEmpty()) {
            return;
        }

        MutableComponent message = Component.literal(m);
        message.withStyle(colour);
        if (m.contains("http") || !url.isEmpty()) {
            if (url.isEmpty()) {
                for (String word : m.split(" ")) {
                    if (word.contains("http")) {
                        url = word;
                        break;
                    }
                }
            }

            if (!url.isEmpty()) {
                Style clickstyle = message.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                message.withStyle(clickstyle);
            }
        }
        sendMessage(source, message, emptyline);
    }
    public static void sendMessage(CommandSourceStack source, MutableComponent message, boolean emptyline) {
        if (emptyline) {
            source.sendSuccess(Component.literal(""), false);
        }

        source.sendSuccess(message, false);
    }

    public static void sendMessage(Player player, String m, ChatFormatting colour, boolean emptyline, String url) {
        if (m.isEmpty()) {
            return;
        }

        MutableComponent message = Component.literal(m);
        message.withStyle(colour);
        if (m.contains("http") || !url.isEmpty()) {
            if (url.isEmpty()) {
                for (String word : m.split(" ")) {
                    if (word.contains("http")) {
                        url = word;
                        break;
                    }
                }
            }

            if (!url.isEmpty()) {
                Style clickstyle = message.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                message.withStyle(clickstyle);
            }
        }

        sendMessage(player, message, emptyline);
    }
    public static void sendMessage(Player player, MutableComponent message, boolean emptyline) {
        if (emptyline) {
            player.sendSystemMessage(Component.literal(""));
        }

        player.sendSystemMessage(message);
    }

    public static void broadcastMessage(Level world, String m, ChatFormatting colour) {
        if (m.isEmpty()) {
            return;
        }

        MutableComponent message = Component.literal(m);
        message.withStyle(colour);
        broadcastMessage(world, message);
    }
    public static void broadcastMessage(Level world, MutableComponent message) {
        MinecraftServer server = world.getServer();
        if (server == null) {
            return;
        }

        for (Player player : server.getPlayerList().getPlayers()) {
            sendMessage(player, message);
        }
    }

    public static void sendMessageToPlayersAround(Level world, BlockPos p, int radius, String message, ChatFormatting colour) {
        if (message.isEmpty()) {
            return;
        }

        for (Entity around : world.getEntities(null, new AABB(p.getX() - radius, p.getY() - radius, p.getZ() - radius, p.getX() + radius, p.getY() + radius, p.getZ() + radius))) {
            if (around instanceof Player) {
                sendMessage((Player) around, message, colour);
            }
        }
    }
    public static void sendMessageToPlayersAround(Level world, BlockPos p, int radius, MutableComponent message) {
        for (Entity around : world.getEntities(null, new AABB(p.getX() - radius, p.getY() - radius, p.getZ() - radius, p.getX() + radius, p.getY() + radius, p.getZ() + radius))) {
            if (around instanceof Player) {
                sendMessage((Player) around, message);
            }
        }
    }
}
