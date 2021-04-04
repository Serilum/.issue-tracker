/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

import net.minecraft.block.Block;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class AreasUtil {
	public static SimpleChannel network;
	
	private static List<String> zoneprefixes = new ArrayList<String>(Arrays.asList("[na]", "[area]", "[region]", "[zone]"));
	private static Field signText = null;
	
	public static AreaObject getAreaSign(World world, BlockPos signpos) {
		if (world.isRemote) {
			return null;
		}
		
		if (signText == null) {
			if (!setSignField()) {
				return null;
			}
		}
		
		HashMap<BlockPos, AreaObject> hm = AreasVariables.areasperworld.get(world);
		if (hm != null) {
			if (hm.containsKey(signpos)) {
				return hm.get(signpos);
			}
		}
		
		TileEntity te = world.getTileEntity(signpos);
		if (te == null) {
			return null;
		}
		
		SignTileEntity signentity;
		try {
			signentity = (SignTileEntity)te;
		}
		catch (ClassCastException ex) {
			return null;
		}

		
		String areaname = "";
		String rgb = "";
		String zoneprefix = "Area";
		int radius = 0;
		boolean customrgb = false;
		
		ITextComponent[] signcontent;
		try {
			signcontent = (ITextComponent[]) signText.get(signentity);
		} catch (Exception ex) {
			return null;
		}
		
		int i = -1;
		for (ITextComponent linecomponent : signcontent) {
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
					signentity.setText(i, new StringTextComponent(line));
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
				for (ITextComponent linecomponent : signcontent) {
					String line = linecomponent.getString();
					if (i == 0) {
						line = "[" + zoneprefix + "] " + radius;
					}
					
					signentity.setText(i, new StringTextComponent(line));
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
	
	public static boolean hasZonePrefix(SignTileEntity signentity) {
		if (signText == null) {
			if (!setSignField()) {
				return false;
			}
		}
		
		ITextComponent[] signcontent;
		try {
			signcontent = (ITextComponent[]) signText.get(signentity);
		} catch (Exception ex) {
			return false;
		}
		
		int i = -1;
		for (ITextComponent linecomponent : signcontent) {
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
	
	public static void enterArea(AreaObject ao, PlayerEntity player) {
		enterArea(ao, player, true);
	}
	public static void enterArea(AreaObject ao, PlayerEntity player, boolean shouldmessage) {
		if (!ao.containsplayers.contains(player)) {
			ao.containsplayers.add(player);
		}
		
		if (shouldmessage) {
			String message = AreasConfigHandler.GENERAL.joinPrefix.get() + ao.areaname + AreasConfigHandler.GENERAL.joinSuffix.get();
			areaChangeMessage(player, message, ao.customrgb);
		}
	}
	public static void exitArea(AreaObject ao, PlayerEntity player) {
		exitArea(ao, player, true);
	}
	public static void exitArea(AreaObject ao, PlayerEntity player, boolean shouldmessage) {
		ao.containsplayers.remove(player);
		
		if (shouldmessage) {
			String message = AreasConfigHandler.GENERAL.leavePrefix.get() + ao.areaname + AreasConfigHandler.GENERAL.leaveSuffix.get();
			areaChangeMessage(player, message, ao.customrgb);
		}
	}
	public static void areaChangeMessage(PlayerEntity player, String message, String rgb) {
		if (AreasConfigHandler.GENERAL.sendChatMessages.get()) {
			StringFunctions.sendMessage(player, message, TextFormatting.DARK_GREEN);
		}
		if (AreasConfigHandler.GENERAL.showHUDMessages.get()) {
			network.sendTo(new PacketToClientShowGUI(message, rgb), ((ServerPlayerEntity)player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);	
		}		
	}
	
	public static Boolean isSignBlock(Block block) {
		if (block instanceof StandingSignBlock || block instanceof WallSignBlock) {
			return true;
		}
		return false;
	}
	public static Boolean isSignItem(Item item) {
		if (isSignBlock(Block.getBlockFromItem(item))) {
			return true;
		}
		return false;
	}
	
	private static boolean setSignField() {
		if (signText == null) {
			for (Field field : SignTileEntity.class.getDeclaredFields()) {
				if (field.toString().contains("signText") || field.toString().contains("field_145915_a")) {
					signText = field;
					break;
				}
			}
			if (signText == null) {
				return false;
			}
			signText.setAccessible(true);
		}
		return true;
	}
	
	private static String getRandomAreaName() {
		return GlobalVariables.areanames.get(GlobalVariables.random.nextInt(GlobalVariables.areanames.size()));
	}
}
