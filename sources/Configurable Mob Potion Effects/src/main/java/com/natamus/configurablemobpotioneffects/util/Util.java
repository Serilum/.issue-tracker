/*
 * This is the latest source code of Configurable Mob Potion Effects.
 * Minecraft version: 1.19.1, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Configurable Mob Potion Effects ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.configurablemobpotioneffects.util;

import com.natamus.collective.functions.NumberFunctions;
import com.natamus.collective.functions.StringFunctions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Util {
	private static String dirpath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "configurablemobpotioneffects";
	private static File dir = new File(dirpath);
	private static File permanentfile = new File(dirpath + File.separator + "permanenteffects.txt");
	private static File damagefile = new File(dirpath + File.separator + "ondamageeffects.txt");
	
	public static HashMap<EntityType<?>, CopyOnWriteArrayList<MobEffectInstance>> mobpermanent = new HashMap<EntityType<?>, CopyOnWriteArrayList<MobEffectInstance>>();
	public static HashMap<EntityType<?>, CopyOnWriteArrayList<MobEffectInstance>> mobdamage = new HashMap<EntityType<?>, CopyOnWriteArrayList<MobEffectInstance>>();
	
	public static void loadMobConfigFile() throws IOException, FileNotFoundException, UnsupportedEncodingException {
		mobpermanent = new HashMap<EntityType<?>, CopyOnWriteArrayList<MobEffectInstance>>();
		mobdamage = new HashMap<EntityType<?>, CopyOnWriteArrayList<MobEffectInstance>>();
		
		PrintWriter permanentwriter = null;
		PrintWriter damagewriter = null;
		if (!dir.isDirectory() || !permanentfile.isFile() || !damagefile.isFile()) {
			dir.mkdirs();
			
			if (!permanentfile.isFile()) {
				permanentwriter = new PrintWriter(dirpath + File.separator + "permanenteffects.txt", "UTF-8");
			}
			if (!damagefile.isFile()) {
				damagewriter = new PrintWriter(dirpath + File.separator + "ondamageeffects.txt", "UTF-8");
			}
		}
		else {
			String permanentcontent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "permanenteffects.txt", new String[0])));
			for (String line : permanentcontent.split("\n")) {
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
				String potioneffects = linespl[1].trim();
				potioneffects = potioneffects.substring(0, potioneffects.length() - 1).trim();
				
				EntityType<?> entitytype = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityrl));
				if (entitytype == null) {
					continue;
				}
				
				mobpermanent.put(entitytype, parseEffectString(potioneffects));
			}
			
			String damagecontent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "ondamageeffects.txt", new String[0])));
			for (String line : damagecontent.split("\n")) {
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
				String potioneffects = linespl[1].trim();
				potioneffects = potioneffects.substring(0, potioneffects.length() - 1).trim();

				EntityType<?> entitytype = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(entityrl));
				if (entitytype == null) {
					continue;
				}
				
				mobdamage.put(entitytype, parseEffectString(potioneffects));
			}
		}
		
		List<String> sortedpotions = new ArrayList<String>();
		List<String> sortedentities = new ArrayList<String>();
		HashMap<String, MobEffect> phm = new HashMap<String, MobEffect>();
		HashMap<String, EntityType<?>> ehm = new HashMap<String, EntityType<?>>();
		
		String emptypermanenteffects = "";
		String emptydamageeffects = "";
		
		if (permanentwriter != null || damagewriter != null) {
			for (MobEffect effect : ForgeRegistries.MOB_EFFECTS) {
				String n = ForgeRegistries.MOB_EFFECTS.getKey(effect).toString().toLowerCase();
				if (n.contains(":")) {
					n = n.split(":")[1];
				}
				
				sortedpotions.add(n);
				phm.put(n, effect);
			}
			for (EntityType<?> entitytype : ForgeRegistries.ENTITY_TYPES) {
				String n = ForgeRegistries.ENTITY_TYPES.getKey(entitytype).toString().toLowerCase();
				if (n.contains(":")) {
					n = n.split(":")[1];
				}
				
				sortedentities.add(n);
				ehm.put(n, entitytype);
			}
			
			Collections.sort(sortedpotions);
			Collections.sort(sortedentities);
			
			for (String effectstring : sortedpotions) {
				MobEffect effect = phm.get(effectstring);
				
				if (emptypermanenteffects != "") {
					emptypermanenteffects += "|";
				}
				if (emptydamageeffects != "") {
					emptydamageeffects += "|";
				}
				
				ResourceLocation rl = ForgeRegistries.MOB_EFFECTS.getKey(effect);
				
				emptypermanenteffects += rl.toString() + ",lvl:0";
				emptydamageeffects += rl.toString() + ",lvl:0,duration:5";
			}
		}
		
		boolean rerun = false;
		if (permanentwriter != null) {
			for (String entitytypestring : sortedentities) {
				EntityType<?> entitytype = ehm.get(entitytypestring);
				
				MobCategory classification = entitytype.getCategory();
				if (!classification.equals(MobCategory.MISC)) {
					ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(entitytype);
					permanentwriter.println("'" + rl.toString() + "'" + " : '" + emptypermanenteffects + "'," + "\n");
					
					mobpermanent.put(entitytype, new CopyOnWriteArrayList<MobEffectInstance>());
				}
			}
			
			permanentwriter.close();
			rerun = true;
		}
		
		if (damagewriter != null) {
			for (String entitytypestring : sortedentities) {
				EntityType<?> entitytype = ehm.get(entitytypestring);
				
				MobCategory classification = entitytype.getCategory();
				if (!classification.equals(MobCategory.MISC)) {
					ResourceLocation rl = ForgeRegistries.ENTITY_TYPES.getKey(entitytype);
					damagewriter.println("'" + rl.toString() + "'" + " : '" + emptydamageeffects + "'," + "\n");
					
					mobdamage.put(entitytype, new CopyOnWriteArrayList<MobEffectInstance>());
				}
			}
			
			damagewriter.close();
			rerun = true;
		}

		if (rerun) {
			loadMobConfigFile();
		}
	}
	
	private static CopyOnWriteArrayList<MobEffectInstance> parseEffectString(String effectstring) {
		CopyOnWriteArrayList<MobEffectInstance> effectinstances = new CopyOnWriteArrayList<MobEffectInstance>();
		
		for (String effectpair : effectstring.split(StringFunctions.escapeSpecialRegexChars("|"))) {
			String[] epspl = effectpair.split(",");
			if (epspl.length < 2) {
				continue;
			}
			
			String effectrlstring = "";
			String lvlstring = "";
			String durationstring = "";
			for (String ep : epspl) {
				if (ep.contains("lvl:")) {
					lvlstring = ep.split(":")[1].trim();
				}
				else if (ep.contains("duration:")) {
					durationstring = ep.split(":")[1].trim();
				}
				else {
					effectrlstring = ep.trim();
				}
			}
			
			if (effectrlstring == "" || lvlstring == "") {
				continue;
			}
			
			if (durationstring == "") {
				durationstring = "0";
			}
			
			MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(effectrlstring));
			if (effect == null) {
				continue;
			}
			
			if (!NumberFunctions.isNumeric(lvlstring) || !NumberFunctions.isNumeric(durationstring)) {
				continue;
			}
			
			int level;
			int duration;
			try {
				level = Integer.parseInt(lvlstring);
				duration = Integer.parseInt(durationstring);
			}
			catch (NumberFormatException ex) {
				continue;
			}
			
			if (level == 0) {
				continue;
			}
			
			if (duration == 0) {
				duration = 100000000;
			}
			
			MobEffectInstance instance = new MobEffectInstance(effect, duration*20, level-1, true, true);
			effectinstances.add(instance);
		}
		
		return effectinstances;
	}
}
