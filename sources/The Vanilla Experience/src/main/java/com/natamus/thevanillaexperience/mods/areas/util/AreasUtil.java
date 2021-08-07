/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.areas.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.natamus.thevanillaexperience.mods.areas.config.AreasConfigHandler;
import com.natamus.thevanillaexperience.mods.areas.network.PacketToClientShowGUI;
import com.natamus.thevanillaexperience.mods.areas.objects.AreaObject;
import com.natamus.thevanillaexperience.mods.areas.objects.AreasVariables;
import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.NumberFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.TileEntityFunctions;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class AreasUtil {
	public static SimpleChannel network;
	
	private static List<String> zoneprefixes = new ArrayList<String>(Arrays.asList("[na]", "[area]", "[region]", "[zone]"));
	private static Field signText = ObfuscationReflectionHelper.findField(SignBlockEntity.class, "f_59720_"); // messages
	
	public static AreaObject getAreaSign(Level world, BlockPos signpos) {
		if (world.isClientSide) {
			return null;
		}

		HashMap<BlockPos, AreaObject> hm = AreasVariables.areasperworld.get(world);
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

		
		String areaname = "";
		String rgb = "";
		String zoneprefix = "Area";
		int radius = 0;
		boolean customrgb = false;
		
		Component[] signcontent;
		try {
			signcontent = (Component[]) signText.get(signentity);
		} catch (Exception ex) {
			return null;
		}
		
		int i = -1;
		for (Component linecomponent : signcontent) {
			i += 1;
			String line = linecomponent.getString();
			
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
			if (possiblergb != "") {
				rgb = possiblergb;
				customrgb = true;
				continue;
			}
			
			if (hasZonePrefix(line)) {
				continue;
			}
			
			if (areaname != "") {
				areaname += " ";
			}
			areaname += line;
		}
		
		boolean setradius = false;
		int maxradius = AreasConfigHandler.GENERAL.radiusAroundPlayerToCheckForSigns.get();
		if (radius > maxradius) {
			radius = maxradius;
			setradius = true;
		}
		
		boolean updatesign = false;		
		if (areaname.trim() == "") {
			if (AreasConfigHandler.GENERAL.giveUnnamedAreasRandomName.get()) {
				List<String> newsigncontentlist = new ArrayList<String>();
				
				newsigncontentlist.add("[" + zoneprefix + "] " + radius);
				if (customrgb) {
					newsigncontentlist.add("[RGB] " + rgb);
				}
				else {
					newsigncontentlist.add("");
				}
				
				areaname = "";
				String randomname = getRandomAreaName();
				for (String word : randomname.split(" ")) {
					if (newsigncontentlist.size() == 4) {
						break;
					}
					newsigncontentlist.add(word);
					if (areaname != "") {
						areaname += " ";
					}
					areaname += word;
				}
				
				i = 0;
				for (String line : newsigncontentlist) {
					signentity.setMessage(i, new TextComponent(line));
					i+=1;
				}
				
				updatesign = true;
			}
			else {
				areaname = "Unnamed area";
			}
		}
		
		if (!updatesign) {
			if (radius == 0 || setradius) {
				i = 0;
				for (Component linecomponent : signcontent) {
					String line = linecomponent.getString();
					if (i == 0) {
						line = "[" + zoneprefix + "] " + radius;
					}
					
					signentity.setMessage(i, new TextComponent(line));
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
		
		return new AreaObject(world, signpos, areaname, radius, rgb);
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
		TextComponent[] signcontent;
		try {
			signcontent = (TextComponent[]) signText.get(signentity);
		} catch (Exception ex) {
			return false;
		}
		
		int i = -1;
		for (Component linecomponent : signcontent) {
			i += 1;
			String line = linecomponent.getString();
			
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
			String message = AreasConfigHandler.GENERAL.joinPrefix.get() + ao.areaname + AreasConfigHandler.GENERAL.joinSuffix.get();
			areaChangeMessage(player, message, ao.customrgb);
		}
	}
	public static void exitArea(AreaObject ao, Player player) {
		exitArea(ao, player, true);
	}
	public static void exitArea(AreaObject ao, Player player, boolean shouldmessage) {
		ao.containsplayers.remove(player);
		
		if (shouldmessage) {
			String message = AreasConfigHandler.GENERAL.leavePrefix.get() + ao.areaname + AreasConfigHandler.GENERAL.leaveSuffix.get();
			areaChangeMessage(player, message, ao.customrgb);
		}
	}
	public static void areaChangeMessage(Player player, String message, String rgb) {
		if (AreasConfigHandler.GENERAL.sendChatMessages.get()) {
			StringFunctions.sendMessage(player, message, ChatFormatting.DARK_GREEN);
		}
		if (AreasConfigHandler.GENERAL.showHUDMessages.get()) {
			network.sendTo(new PacketToClientShowGUI(message, rgb), ((ServerPlayer)player).connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);	
		}		
	}
	
	public static Boolean isSignBlock(Block block) {
		if (block instanceof StandingSignBlock || block instanceof WallSignBlock) {
			return true;
		}
		return false;
	}
	public static Boolean isSignItem(Item item) {
		if (isSignBlock(Block.byItem(item))) {
			return true;
		}
		return false;
	}
	
	private static String getRandomAreaName() {
		return GlobalVariables.areanames.get(GlobalVariables.random.nextInt(GlobalVariables.areanames.size()));
	}
}