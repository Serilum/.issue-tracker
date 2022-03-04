/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.x, mod version: 4.20.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective_fabric.functions;

import com.natamus.collective_fabric.data.GlobalVariables;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringFunctions {
	// START: DO functions
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
		
		if (emptyline) {
			source.sendSuccess(new TextComponent(""), false);
		}
		
		TextComponent message = new TextComponent(m);
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
		source.sendSuccess(message, false);
	}

	public static void sendMessage(Player player, String m, ChatFormatting colour, boolean emptyline, String url) {
		if (m.isEmpty()) {
			return;
		}
		
		if (emptyline) {
			player.sendMessage(new TextComponent(""), player.getUUID());
		}
		
		TextComponent message = new TextComponent(m);
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
		player.sendMessage(message, player.getUUID());
	}
	
	public static void broadcastMessage(Level world, String m, ChatFormatting colour) {
		if (m.isEmpty()) {
			return;
		}
		
		TextComponent message = new TextComponent(m);
		message.withStyle(colour);
		MinecraftServer server = world.getServer();
		if (server == null) {
			return;
		}
		
		for (Player player : server.getPlayerList().getPlayers()) {
			sendMessage(player, m, colour);
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

	public static String capitalizeFirst(String string) {
		StringBuilder sb = new StringBuilder(string);
		for(int i=0; i < sb.length(); i++) {
			if(i == 0 || sb.charAt(i-1) == ' ') {
				sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
			}
		}
		return sb.toString();
	}
	
	public static String capitalizeEveryWord(String text) {
		if (text.length() == 0) {
			return text;
		}
		
		char[] chars = text.toLowerCase().toCharArray();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} 
			else if (!(Character.isDigit(chars[i]) || Character.isLetter(chars[i]))) { 
				found = false; 
			}
		}
		return String.valueOf(chars);
	}
	
	public static String escapeSpecialRegexChars(String str) {
		return Pattern.compile("[{}()\\[\\].+*?^$\\\\|]").matcher(str).replaceAll("\\\\$0");
	}
	// END: DO functions
	
	
	// START: GET functions
	public static String getRandomName(boolean malenames, boolean femalenames) {
		List<String> allnames;
		if (malenames && femalenames) {
			allnames = Stream.concat(GlobalVariables.femalenames.stream(), GlobalVariables.malenames.stream()).collect(Collectors.toList());
		}
		else if (femalenames) {
			allnames = GlobalVariables.femalenames;
		}
		else if (malenames) {
			allnames = GlobalVariables.malenames;
		}
		else {
			return "";
		}
		
	    String name = allnames.get(GlobalVariables.random.nextInt(allnames.size())).toLowerCase();
	    return capitalizeEveryWord(name);
	}
	
	public static String getPCLocalTime(boolean twentyfour, boolean showseconds) {
		String time;
		LocalDateTime now = LocalDateTime.now();
		if (showseconds) {
			if (twentyfour) {
				time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
			}
			else {
				time = now.format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
			}
		}
		else {
			if (twentyfour) {
				time = now.format(DateTimeFormatter.ofPattern("HH:mm"));
			}
			else {
				time = now.format(DateTimeFormatter.ofPattern("hh:mm a"));
			}
		}
		return time;
	}
	// END: GET functions
	
	// Util
	public static int sequenceCount(String text, String sequence) {
		Pattern pattern = Pattern.compile(sequence);
		Matcher matcher = pattern.matcher(text);

		int count = 0;
		while (matcher.find()) {
		    count++;
		}
	    return count;
	}
	
	public static String joinListWithCommaAnd(List<String> inputlist) {
		if (inputlist.size() == 0) {
			return "";
		}
		if (inputlist.size() == 1) {
			return inputlist.get(0);
		}
		
		List<String> list = new ArrayList<String>(inputlist);
		String lastelement = list.get(list.size()-1);
		list.remove(list.size()-1);
		
		String initial = String.join(", ", list);
		return initial + " and " + lastelement;
	}
}