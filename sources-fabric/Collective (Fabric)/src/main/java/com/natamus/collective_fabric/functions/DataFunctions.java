/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.8.
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

package com.natamus.collective_fabric.functions;

import net.minecraft.SharedConstants;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataFunctions {
	public static String readStringFromURL(String requestURL) {
		String data = "";
	    try (Scanner scanner = new Scanner(new URL(requestURL).openStream(), StandardCharsets.UTF_8.toString())) {
	        scanner.useDelimiter("\\A");
	        data = scanner.hasNext() ? scanner.next() : "";
	    }
	    catch(Exception ignored) {}
	    
	    return data;
	}

	@SuppressWarnings("deprecation")
	public static String getCurrentMinecraftVersion() {
		return SharedConstants.VERSION_STRING;
	}

	public static String getModDirectory() {
		return System.getProperty("user.dir") + File.separator + "mods";
	}

	public static List<String> getInstalledModJars() {
		List<String> installedmods = new ArrayList<String>();

		File mainFolder = new File(getModDirectory());
		File[] listOfMainFiles = mainFolder.listFiles();
		File subFolder = new File(getModDirectory() + File.separator + getCurrentMinecraftVersion());
		File[] listOfSubFiles = subFolder.listFiles();

		for (File file : ArrayUtils.addAll(listOfMainFiles, listOfSubFiles)) {
			if (file.isFile()) {
				String filename = file.getName().replaceAll(" +\\([0-9]+\\)", "");
				installedmods.add(filename);
			}
		}

		return installedmods;
	}
	
	public static byte setBit(byte b, int i, boolean bo) {
		if (bo) {
			b = (byte)(b | i);
		}
		else {
			b = (byte)(b & ~i);
		}
		
		return b;
	}
}
