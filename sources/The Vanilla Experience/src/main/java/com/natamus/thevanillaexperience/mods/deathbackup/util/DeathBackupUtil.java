/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.deathbackup.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.server.level.ServerLevel;

public class DeathBackupUtil {
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