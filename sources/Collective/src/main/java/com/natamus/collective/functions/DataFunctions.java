/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.64.
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
