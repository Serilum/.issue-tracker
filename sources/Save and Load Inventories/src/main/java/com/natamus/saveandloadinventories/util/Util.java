/*
 * This is the latest source code of Save and Load Inventories.
 * Minecraft version: 1.19.1, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.saveandloadinventories.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Util {
	private static String workspace_path = System.getProperty("user.dir");
	private static final String dirpath = workspace_path + File.separator + "config" + File.separator + "saveandloadinventories";
	
	public static boolean writeGearStringToFile(String filename, String gearstring) {
		File dir = new File(dirpath);
		dir.mkdirs();
		
		try {
			PrintWriter writer = new PrintWriter(dirpath + File.separator + filename + ".txt", "UTF-8");
			writer.println(gearstring);
			writer.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static String getGearStringFromFile(String filename) {
		File dir = new File(dirpath);
		File file = new File(dirpath + File.separator + filename + ".txt");
		
		String gearstring = "";
		if (dir.isDirectory() && file.isFile()) {
			try {
				gearstring = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + filename + ".txt", new String[0])));
			} catch (IOException e) { }
		}
		
		return gearstring;
	}
	
	public static String getListOfInventories() {
		String inventories = "";
		
		File folder = new File(dirpath);
		if (!folder.isDirectory()) {
			return inventories;
		}
		
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles.length == 0) {
			return inventories;
		}
		
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if (inventories != "") {
					inventories += ", ";
				}
				inventories += listOfFiles[i].getName().replace(".txt", "");
			}
		}
		
		return inventories;
	}
}