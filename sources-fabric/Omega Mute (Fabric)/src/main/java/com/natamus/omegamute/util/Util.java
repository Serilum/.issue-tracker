/*
 * This is the latest source code of Omega Mute.
 * Minecraft version: 1.19.1, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Omega Mute ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.omegamute.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.natamus.omegamute.events.MuteEvent;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class Util {
	// MUTED VALUE
	// -1 == no mute
	// 0 == always mute
	// > 0 == cull sound for x seconds
	
	private static String dirpath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "omegamute";
	private static File dir = new File(dirpath);
	private static File file = new File(dirpath + File.separator + "soundmap.txt");
	
	public static boolean loadSoundFile() throws IOException, FileNotFoundException, UnsupportedEncodingException {		
		MuteEvent.ismutedsoundmap = new HashMap<String, Integer>();
		
		if (!dir.isDirectory() || !file.isFile()) {
			generateSoundFile();
			return false;
		}
		
		String sounds = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "soundmap.txt", new String[0])));
		for (String sound : sounds.split("\n")) {
			if (sound.length() < 4) {
				continue;
			}
			
			String soundname = sound.replace(",", "");
			int mutedvalue = -1;
			
			if (soundname.startsWith("!")) {
				mutedvalue = 0;
				soundname = soundname.substring(1);
			}
			else if (soundname.contains("-")) {
				try {
					String[] soundsplit = soundname.split("-");
					if (soundsplit.length == 2) {
						mutedvalue = Integer.parseInt(soundsplit[0]);
						soundname = soundsplit[1];
					}
				}
				catch (NumberFormatException ex) {}
			}
			
			MuteEvent.ismutedsoundmap.put(soundname.trim(), mutedvalue);
		}
		return true;
	}
	
	private static void updateSoundFile() {
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(dirpath + File.separator + "soundmap.txt"), true);
			
			List<String> keyset = new ArrayList<String>(MuteEvent.ismutedsoundmap.keySet());
			Collections.sort(keyset);
			for (String soundname : keyset) {
				int mutedvalue = MuteEvent.ismutedsoundmap.get(soundname);
				if (mutedvalue >= 0) {
					if (mutedvalue == 0) {
						soundname = "!" + soundname;
					}
					else {
						soundname = mutedvalue + "-" + soundname;
					}
				}
				writer.println(soundname + ",");
			}

			writer.close();	
		} catch (Exception ex) { }
	}
	
	private static void generateSoundFile() throws FileNotFoundException, UnsupportedEncodingException {
		List<String> soundnames = new ArrayList<String>();
		
		Set<ResourceLocation> soundlocations = Registry.SOUND_EVENT.keySet();
		for (ResourceLocation soundlocation : soundlocations) {
			SoundEvent sound = Registry.SOUND_EVENT.get(soundlocation);
			soundnames.add(sound.getLocation().toString().replace("minecraft:", ""));
		}
		
		Collections.sort(soundnames);
		
		dir.mkdirs();
		
		PrintWriter writer = new PrintWriter(dirpath + File.separator + "soundmap.txt", "UTF-8");
		for (String soundname : soundnames) {
			MuteEvent.ismutedsoundmap.put(soundname, -1);
			writer.println(soundname + ",");
		}

		writer.close();
	}
	
	public static HashMap<String, Integer> getMutedSounds() {
		HashMap<String, Integer> mutedsounds = new HashMap<String, Integer>();
		
		List<String> keyset = new ArrayList<String>(MuteEvent.ismutedsoundmap.keySet());
		Collections.sort(keyset);
		for (String soundname : keyset) {
			int mutedvalue = MuteEvent.ismutedsoundmap.get(soundname);
			if (mutedvalue >= 0) {
				mutedsounds.put(soundname, mutedvalue);
			}
		}
		
		return mutedsounds;
	}
	
	public static List<String> muteWildcard(String wildcard, int culltime) {
		if (!dir.isDirectory() || !file.isFile()) {
			try {
				generateSoundFile();
			} catch (Exception e) {}
		}		

		List<String> mutedsounds = new ArrayList<String>();
		
		try {
			String sounds = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "soundmap.txt", new String[0])));

			for (String sound : sounds.split("\n")) {
				if (sound.length() < 4) {
					continue;
				}
				
				String soundname = sound.replace(",", "");
				int mutedvalue = -1;
				
				if (soundname.startsWith("!")) {
					mutedvalue = 0;
					soundname = soundname.substring(1);
				}
				else if (soundname.contains("-")) {
					try {
						String[] soundsplit = soundname.split("-");
						if (soundsplit.length == 2) {
							mutedvalue = Integer.parseInt(soundsplit[0]);
							soundname = soundsplit[1];
						}
					}
					catch (NumberFormatException ex) {}
				}
				
				if (soundname.toLowerCase().contains(wildcard.toLowerCase())) {
					mutedvalue = culltime;
					mutedsounds.add(soundname.trim());
				}
				
				MuteEvent.ismutedsoundmap.put(soundname.trim(), mutedvalue);
			}
		} catch (Exception ex) { }
		
		if (mutedsounds.size() > 0) {
			updateSoundFile();
		}
		return mutedsounds;
	}
	
	public static List<String> unmuteWildcard(String wildcard) {
		List<String> unmutedsounds = new ArrayList<String>();
		
		try {
			String sounds = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "soundmap.txt", new String[0])));

			for (String sound : sounds.split("\n")) {
				if (sound.length() < 4) {
					continue;
				}
				
				String soundname = sound.replace(",", "");
				int mutedvalue = -1;
				
				if (soundname.startsWith("!")) {
					mutedvalue = 0;
					soundname = soundname.substring(1);
				}
				else if (soundname.contains("-")) {
					try {
						String[] soundsplit = soundname.split("-");
						if (soundsplit.length == 2) {
							mutedvalue = Integer.parseInt(soundsplit[0]);
							soundname = soundsplit[1];
						}
					}
					catch (NumberFormatException ex) {}
				}
				
				if (soundname.toLowerCase().contains(wildcard.toLowerCase())) {
					mutedvalue = -1;
					unmutedsounds.add(soundname.trim());
				}
				
				MuteEvent.ismutedsoundmap.put(soundname.trim(), mutedvalue);
			}
		} catch (Exception ex) { }
		
		if (unmutedsounds.size() > 0) {
			updateSoundFile();
		}
		return unmutedsounds;
	}
}