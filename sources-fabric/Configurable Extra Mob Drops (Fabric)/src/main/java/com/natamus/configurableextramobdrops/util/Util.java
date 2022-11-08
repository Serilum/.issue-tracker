/*
 * This is the latest source code of Configurable Extra Mob Drops.
 * Minecraft version: 1.19.2, mod version: 2.5.
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

package com.natamus.configurableextramobdrops.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective_fabric.functions.DataFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Util {
	private static String dirpath = DataFunctions.getConfigDirectory() + File.separator + "configurableextramobdrops";
	private static File dir = new File(dirpath);
	private static File file = new File(dirpath + File.separator + "mobdropconfig.txt");
	
	public static HashMap<EntityType<?>, CopyOnWriteArrayList<ItemStack>> mobdrops = new HashMap<EntityType<?>, CopyOnWriteArrayList<ItemStack>>();
	private static List<EntityType<?>> specialmiscmobs = new ArrayList<EntityType<?>>(Arrays.asList(EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.VILLAGER));
	
	public static void loadMobConfigFile() throws IOException, FileNotFoundException, UnsupportedEncodingException {
		mobdrops = new HashMap<EntityType<?>, CopyOnWriteArrayList<ItemStack>>();
		
		PrintWriter writer = null;
		if (!dir.isDirectory() || !file.isFile()) {
			dir.mkdirs();
			writer = new PrintWriter(dirpath + File.separator + "mobdropconfig.txt", "UTF-8");
		}
		else {
			String configcontent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "mobdropconfig.txt", new String[0])), "UTF-8");
			for (String line : configcontent.split("\n")) {
				if (line.trim().endsWith(",")) {
					line = line.trim();
					line = line.substring(0, line.length() - 1).trim();
				}
				
				if (line.length() < 5) {
					continue;
				}
				
				if (!line.contains("' : '")) {
					continue;
				}
				
				String[] linespl = line.split("' : '");
				if (linespl.length < 2) {
					continue;
				}
				
				String entityrl = linespl[0].substring(1).trim();
				String itemstring = linespl[1].trim();
				itemstring = itemstring.substring(0, itemstring.length() - 1).trim();
				
				EntityType<?> entitytype = Registry.ENTITY_TYPE.get(new ResourceLocation(entityrl));
				if (entitytype == null) {
					continue;
				}
				
				CopyOnWriteArrayList<ItemStack> thedrops = new CopyOnWriteArrayList<ItemStack>(); 
				if (itemstring.length() > 3) {
					for (String itemdata : itemstring.split(StringFunctions.escapeSpecialRegexChars("|||"))) {
						ItemStack itemstack = null;
						try {
							CompoundTag newnbt = TagParser.parseTag(itemdata);
							itemstack = ItemStack.of(newnbt);
						} catch (CommandSyntaxException e) {}
						
						if (itemstack != null) {
							thedrops.add(itemstack.copy());
						}
					}
				}
				
				mobdrops.put(entitytype, thedrops);
			}
		}
		
		if (writer != null) {
			for (ResourceLocation rl : Registry.ENTITY_TYPE.keySet()) {
				EntityType<?> entitytype = Registry.ENTITY_TYPE.get(rl);
				MobCategory classification = entitytype.getCategory();
				if (!classification.equals(MobCategory.MISC) || specialmiscmobs.contains(entitytype)) {
					writer.println("'" + rl.toString() + "'" + " : '',");
					
					mobdrops.put(entitytype, new CopyOnWriteArrayList<ItemStack>());
				}
			}
			
			writer.close();
		}
	}
	
	public static boolean writeDropsMapToFile() throws FileNotFoundException, UnsupportedEncodingException {
		if (!dir.isDirectory() || !file.isFile()) {
			dir.mkdirs();
		}
		
		PrintWriter writer = new PrintWriter(dirpath + File.separator + "mobdropconfig.txt", "UTF-8");
		
		for (ResourceLocation rl : Registry.ENTITY_TYPE.keySet()) {
			EntityType<?> entitytype = Registry.ENTITY_TYPE.get(rl);
			MobCategory classification = entitytype.getCategory();
			if (!classification.equals(MobCategory.MISC) || specialmiscmobs.contains(entitytype)) {
				String itemdata = "";
				if (mobdrops.containsKey(entitytype)) {
					CopyOnWriteArrayList<ItemStack> drops = mobdrops.get(entitytype);
					if (drops.size() > 0) {
						for (ItemStack drop : drops) {
							if (itemdata != "" ) {
								itemdata += "|||";
							}
							
							CompoundTag nbt = new CompoundTag();
							nbt = drop.save(nbt);
							String nbtstring = nbt.toString();
							
							itemdata += nbtstring;
						}
					}
				}
				
				writer.println("'" + rl.toString() + "'" + " : '" + itemdata + "',");
			}
		}
		
		writer.close();
		return true;
	}
}
