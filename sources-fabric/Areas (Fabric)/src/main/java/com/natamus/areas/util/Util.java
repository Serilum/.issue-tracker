/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.2, mod version: 3.1.
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

package com.natamus.areas.util;

import com.natamus.areas.config.ConfigHandler;
import com.natamus.areas.network.PacketToClientShowGUI;
import com.natamus.areas.objects.AreaObject;
import com.natamus.areas.objects.Variables;
import com.natamus.collective_fabric.data.GlobalVariables;
import com.natamus.collective_fabric.functions.NumberFunctions;
import com.natamus.collective_fabric.functions.SignFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.collective_fabric.functions.TileEntityFunctions;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Util {
	private static List<String> zoneprefixes = new ArrayList<String>(Arrays.asList("[na]", "[area]", "[region]", "[zone]"));
	
	public static AreaObject getAreaSign(Level world, BlockPos signpos) {
		if (world.isClientSide) {
			return null;
		}

		HashMap<BlockPos, AreaObject> hm = Variables.areasperworld.get(world);
		if (hm != null) {
			if (hm.containsKey(signpos)) {
				return hm.get(signpos);
			}
		}
		
		BlockEntity te = world.getBlockEntity(signpos);
		if (te == null) {
			return null;
		}
		
		SignBlockEntity signentity;
		try {
			signentity = (SignBlockEntity)te;
		}
		catch (ClassCastException ex) {
			return null;
		}

		
		StringBuilder areaname = new StringBuilder();
		String rgb = "";
		String zoneprefix = "Area";
		int radius = 0;
		boolean customrgb = false;

		List<String> signlines = SignFunctions.getSignText(signentity);

		int i = -1;
		for (String line : signlines) {
			i += 1;
			if (i == 0 && !hasZonePrefix(line)) {
				return null;
			}
			
			if (line.length() < 1) {
				continue;
			}
			
			for (String zpx : zoneprefixes) {
				if (line.toLowerCase().contains(zpx)) {
					zoneprefix = StringFunctions.capitalizeFirst(zpx.replace("[", "").replace("]", ""));
					break;
				}
			}
			
			Integer possibleradius = getZonePrefixgetRadius(line.toLowerCase());
			if (possibleradius >= 0) {
				radius = possibleradius;
				continue;
			}
			
			String possiblergb = getZoneRGB(line.toLowerCase());
			if (!possiblergb.equals("")) {
				rgb = possiblergb;
				customrgb = true;
				continue;
			}
			
			if (hasZonePrefix(line)) {
				continue;
			}
			
			if (!areaname.toString().equals("")) {
				areaname.append(" ");
			}
			areaname.append(line);
		}
		
		boolean setradius = false;
		int maxradius = ConfigHandler.radiusAroundPlayerToCheckForSigns.getValue();
		if (radius > maxradius) {
			radius = maxradius;
			setradius = true;
		}
		
		boolean updatesign = false;		
		if (areaname.toString().trim().equals("")) {
			if (ConfigHandler.giveUnnamedAreasRandomName.getValue()) {
				List<String> newsigncontentlist = new ArrayList<String>();
				
				newsigncontentlist.add("[" + zoneprefix + "] " + radius);
				if (customrgb) {
					newsigncontentlist.add("[RGB] " + rgb);
				}
				else {
					newsigncontentlist.add("");
				}
				
				areaname = new StringBuilder();
				String randomname = getRandomAreaName();
				for (String word : randomname.split(" ")) {
					if (newsigncontentlist.size() == 4) {
						break;
					}
					newsigncontentlist.add(word);
					if (!areaname.toString().equals("")) {
						areaname.append(" ");
					}
					areaname.append(word);
				}
				
				i = 0;
				for (String line : newsigncontentlist) {
					signentity.setMessage(i, Component.literal(line));
					i+=1;
				}
				
				updatesign = true;
			}
			else {
				areaname = new StringBuilder("Unnamed area");
			}
		}
		
		if (!updatesign) {
			if (radius == 0 || setradius) {
				i = 0;
				for (String line : signlines) {
					if (i == 0) {
						line = "[" + zoneprefix + "] " + radius;
					}
					
					signentity.setMessage(i, Component.literal(line));
					i+=1;
				}
				
				updatesign = true;
			}
		}
		
		if (updatesign) {
			TileEntityFunctions.updateTileEntity(world, signpos, signentity);
		}
		if (radius < 0) {
			return null;
		}
		
		return new AreaObject(world, signpos, areaname.toString(), radius, rgb);
	}
	
	private static boolean hasZonePrefix(String line) {
		for (String prefix : zoneprefixes) {
			if (line.toLowerCase().startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasZonePrefix(SignBlockEntity signentity) {
		int i = -1;
		for (String line : SignFunctions.getSignText(signentity)) {
			i += 1;

			if (i == 0 && hasZonePrefix(line)) {
				return true;
			}
			break;
		}
		
		return false;
	}
	
	private static Integer getZonePrefixgetRadius(String line) {
		for (String prefix : zoneprefixes) {
			if (line.startsWith(prefix)) {
				String[] linespl = line.split("\\]");
				if (linespl.length < 2) {
					return -1;
				}
				
				String rest = linespl[1].trim();
				if (NumberFunctions.isNumeric(rest)) {
					return Integer.parseInt(rest);
				}
			}
		}
		
		return -1;
	}
	
	private static String getZoneRGB(String line) {
		String prefix = "[rgb]";
		if (line.startsWith(prefix)) {
			String[] linespl = line.split("\\]");
			if (linespl.length < 2) {
				return "";
			}
			
			String rest = linespl[1].replace(" ", "");
			String[] restspl = rest.split(",");
			if (restspl.length != 3) {
				return "";
			}
			
			for (String value : restspl) {
				if (!NumberFunctions.isNumeric(value)) {
					return "";
				}
			}	
			return rest;
		}
		return "";
	}
	
	public static void enterArea(AreaObject ao, Player player) {
		enterArea(ao, player, true);
	}
	public static void enterArea(AreaObject ao, Player player, boolean shouldmessage) {
		if (!ao.containsplayers.contains(player)) {
			ao.containsplayers.add(player);
		}
		
		if (shouldmessage) {
			String message = ConfigHandler.joinPrefix.getValue() + ao.areaname + ConfigHandler.joinSuffix.getValue();
			areaChangeMessage(player, message, ao.customrgb);
		}
	}
	public static void exitArea(AreaObject ao, Player player) {
		exitArea(ao, player, true);
	}
	public static void exitArea(AreaObject ao, Player player, boolean shouldmessage) {
		ao.containsplayers.remove(player);
		
		if (shouldmessage) {
			String message = ConfigHandler.leavePrefix.getValue() + ao.areaname + ConfigHandler.leaveSuffix.getValue();
			areaChangeMessage(player, message, ao.customrgb);
		}
	}
	public static void areaChangeMessage(Player player, String message, String rgb) {
		if (ConfigHandler.sendChatMessages.getValue()) {
			StringFunctions.sendMessage(player, message, ChatFormatting.DARK_GREEN);
		}
		if (ConfigHandler.showHUDMessages.getValue()) {
			ServerPlayNetworking.send((ServerPlayer)player, Variables.networkchannel, PacketToClientShowGUI.createBuffer(message, rgb));
		}		
	}
	
	public static Boolean isSignBlock(Block block) {
		return block instanceof StandingSignBlock || block instanceof WallSignBlock;
	}
	public static Boolean isSignItem(Item item) {
		return isSignBlock(Block.byItem(item));
	}
	
	private static String getRandomAreaName() {
		return GlobalVariables.areanames.get(GlobalVariables.random.nextInt(GlobalVariables.areanames.size()));
	}
}
