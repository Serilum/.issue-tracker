/*
 * This is the latest source code of Starter Kit.
 * Minecraft version: 1.19.2, mod version: 3.9.
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

package com.natamus.starterkit.util;

import com.natamus.collective.functions.DataFunctions;
import com.natamus.collective.functions.NumberFunctions;
import com.natamus.collective.functions.PlayerFunctions;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Util {
	public static String startergearstring = "";

	private static final String dirpath = DataFunctions.getConfigDirectory() + File.separator + "starterkit";
	
	public static void getOrCreateGearConfig(boolean first) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		File dir = new File(dirpath);
		File file = new File(dirpath + File.separator + "starterkit.txt");
		
		if (dir.isDirectory() && file.isFile()) {
			String configstring = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "starterkit.txt", new String[0])));
			if (configstring.startsWith("'")) { // nbt
				startergearstring = configstring;
			}
			else {
				HashMap<String, ItemStack> simplegear = new HashMap<String, ItemStack>();
				
				for (String line : configstring.split(",")) {
					String[] linespl = line.replace(" ", "").trim().split(":\"");
					if (linespl.length < 2) {
						continue;
					}
					
					int amount = 1;
					String slotstring = linespl[0].replace("\"", "").trim();
					String itemstring = linespl[1].replace("\"", "").trim();
					if (itemstring.contains("-")) {
						String[] itemspl = itemstring.split("-");
						if (itemspl.length > 1) {
							itemstring = itemspl[0];
							if (NumberFunctions.isNumeric(itemspl[1])) {
								amount = Integer.parseInt(itemspl[1]);
							}
						}
					}
					
					Item item = null;
					ResourceLocation itemloc = new ResourceLocation(itemstring);
					
					if (ForgeRegistries.ITEMS.containsKey(itemloc)) {
						item = ForgeRegistries.ITEMS.getValue(itemloc);
					}
					else if (ForgeRegistries.BLOCKS.containsKey(itemloc)) {
						Block block = ForgeRegistries.BLOCKS.getValue(itemloc);
						item = block.asItem();
					}
					else {
						continue;
					}
					
					if (item == null) {
						continue;
					}
					
					simplegear.put(slotstring, new ItemStack(item, amount));
				}
				
				startergearstring = PlayerFunctions.getPlayerGearStringFromHashMap(simplegear);
			}
		}
		else {
			dir.mkdirs();

			PrintWriter writer = new PrintWriter(dirpath + File.separator + "starterkit.txt", "UTF-8");
			writer.println('"' + "offhand" + '"' + " : " + '"' + "minecraft:shield" + '"' + ",");
			writer.println('"' + "head" + '"' + " : " + '"' + "" + '"' + ",");
			writer.println('"' + "chest" + '"' + " : " + '"' + "" + '"' + ",");
			writer.println('"' + "legs" + '"' + " : " + '"' + "" + '"' + ",");
			writer.println('"' + "feet" + '"' + " : " + '"' + "minecraft:leather_boots" + '"' + ",");
			
			List<ItemStack> emptyinventory = NonNullList.withSize(36, ItemStack.EMPTY);
			for (int i = 0; i < emptyinventory.size(); i++) {
				String itemstring = "";
				if (i == 0) {
					itemstring = "minecraft:wooden_sword";
				}
				else if (i == 1) {
					itemstring = "minecraft:bread-9";
				}
				writer.println(i + " : " + '"' + itemstring + '"' + ",");
			}
			
			writer.close();
			
			if (first) {
				getOrCreateGearConfig(false);
			}
		}
	}
	
	public static boolean createGearConfigFromGearString(String gearstring) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		File dir = new File(dirpath);
		File file = new File(dirpath + File.separator + "starterkit.txt");
		
		if (dir.isDirectory() && file.isFile()) {
			File renamedfile = new File(dirpath + File.separator + "starterkit-old.txt");
			int r = 0;
			while (renamedfile.exists()) {
				renamedfile = new File(dirpath + File.separator + "starterkit-old_" + r + ".txt");
				r+=1;
			}
			
			file.renameTo(renamedfile);
		}
		else {
			dir.mkdirs();
		}
		
		PrintWriter writer;
		writer = new PrintWriter(dirpath + File.separator + "starterkit.txt", "UTF-8");
		writer.println(gearstring);
		writer.close();
		
		getOrCreateGearConfig(false);
		return true;
	}
	
	public static void setStarterKit(Player player) {
		if (startergearstring == "") {
			return;
		}
		
		PlayerFunctions.setPlayerGearFromString(player, startergearstring);
	}
	
	public static boolean processNewGearString(String gearstring) {
		try {
			if (createGearConfigFromGearString(gearstring)) {
				return true;
			}
		} catch (Exception e) { 
			return false;
		}
		return false;
	}
}