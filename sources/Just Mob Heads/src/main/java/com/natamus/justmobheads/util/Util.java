/*
 * This is the latest source code of Just Mob Heads.
 * Minecraft version: 1.19.2, mod version: 5.9.
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

package com.natamus.justmobheads.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Util {
	private static String dirpath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "justmobheads";
	private static File dir = new File(dirpath);
	private static File file = new File(dirpath + File.separator + "headchances.txt");
	
	public static boolean generateChanceConfig(Map<String, Double> defaultchances) throws IOException {
		HeadData.headchances = new HashMap<String, Double>();
		
		PrintWriter writer;
		if (!dir.isDirectory() || !file.isFile()) {
			if (!dir.mkdirs()) {
				return false;
			}
			writer = new PrintWriter(dirpath + File.separator + "headchances.txt", StandardCharsets.UTF_8);
		}
		else {
			String content = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "headchances.txt")));
			String[] cspl = content.replaceAll("\n", "").split(",");
			for (String line : cspl) {
				String[] linespl = line.replace("\"", "").replaceAll(" ", "").trim().split(":");
				if (linespl.length < 2) {
					continue;
				}
				
				String mobname = linespl[0];
				double chancevalue;
				try {
					chancevalue = Double.parseDouble(linespl[1]);
				}
				catch (NumberFormatException ex) {
					continue;
				}
				
				HeadData.headchances.put(mobname, chancevalue);
			}
			
			List<String> appendkeys = new ArrayList<String>();
			
			List<String> defaultkeys = new ArrayList<String>(defaultchances.keySet());
			Collections.sort(defaultkeys);
			for (String mobname : defaultkeys) {
				if (!HeadData.headchances.containsKey(mobname)) {
					appendkeys.add(mobname);
				}
			}
			
			if (appendkeys.size() > 0) {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(dirpath + File.separator + "headchances.txt", true)));
				
				Collections.sort(appendkeys);
				for (String mobname : appendkeys) {
					Double chancevalue = defaultchances.get(mobname);
					
					writer.println('"' + mobname + '"' + " : " + chancevalue + ",");
					HeadData.headchances.put(mobname, chancevalue);
				}
				
				writer.close();
			}
			
			return true;
		}
		
		if (writer != null) {
			List<String> defaultkeys = new ArrayList<String>(defaultchances.keySet());
			Collections.sort(defaultkeys);
			for (String mobname : defaultkeys) {
				Double chancevalue = defaultchances.get(mobname);
				
				writer.println('"' + mobname + '"' + " : " + chancevalue + ",");
				HeadData.headchances.put(mobname, chancevalue);
			}
			
			writer.close();
		}
		
		return false;
	}
}
