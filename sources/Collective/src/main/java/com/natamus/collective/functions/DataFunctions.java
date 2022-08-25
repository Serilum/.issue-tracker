/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.49.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective.functions;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

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
	
	public static List<String> getInstalledModJars() {
		List<String> installedmods = new ArrayList<String>();
		
		List<IModInfo> mods = ModList.get().getMods();
		for (IModInfo modinfo : mods) {
			String filename = modinfo.getOwningFile().getFile().getFileName().replaceAll(" +\\([0-9]+\\)", "");
			installedmods.add(filename);
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
