/*
 * This is the latest source code of Death Backup.
 * Minecraft version: 1.19.x, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Death Backup ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.deathbackup.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.natamus.collective_fabric.functions.WorldFunctions;

import net.minecraft.server.level.ServerLevel;

public class Util {
	public static boolean writeGearStringToFile(ServerLevel serverworld, String playername, String filename, String gearstring) {
		String deathbackuppath = WorldFunctions.getWorldPath(serverworld) + File.separator + "data" + File.separator + "deathbackup" + File.separator + playername;
		File dir = new File(deathbackuppath);
		dir.mkdirs();
		
		try {
			PrintWriter writer = new PrintWriter(deathbackuppath + File.separator + filename + ".txt", "UTF-8");
			writer.println(gearstring);
			writer.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static String getGearStringFromFile(ServerLevel serverworld, String playername, String filename) {
		String deathbackuppath = WorldFunctions.getWorldPath(serverworld) + File.separator + "data" + File.separator + "deathbackup" + File.separator + playername;
		File dir = new File(deathbackuppath);
		File file = new File(deathbackuppath + File.separator + filename + ".txt");
		
		String gearstring = "";
		if (dir.isDirectory() && file.isFile()) {
			try {
				gearstring = new String(Files.readAllBytes(Paths.get(deathbackuppath + File.separator + filename + ".txt", new String[0])));
			} catch (IOException e) { }
		}
		
		return gearstring;
	}
	
	public static List<String> getListOfBackups(ServerLevel serverworld, String playername) {
		List<String> backups = new ArrayList<String>();
		
		String deathbackuppath = WorldFunctions.getWorldPath(serverworld) + File.separator + "data" + File.separator + "deathbackup" + File.separator + playername;
		File folder = new File(deathbackuppath);
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles == null) {
			return backups;
		}
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				backups.add(listOfFiles[i].getName().replace(".txt", ""));
			}
		}
		
		Collections.reverse(backups);
		return backups;
	}
}