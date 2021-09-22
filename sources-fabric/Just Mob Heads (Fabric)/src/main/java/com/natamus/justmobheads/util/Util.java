/*
 * This is the latest source code of Just Mob Heads.
 * Minecraft version: 1.17.x, mod version: 4.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Just Mob Heads ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.justmobheads.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
	private static String dirpath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "justmobheads";
	private static File dir = new File(dirpath);
	private static File file = new File(dirpath + File.separator + "headchances.txt");
	
	public static boolean generateChanceConfig(Map<String, Double> defaultchances) throws IOException, FileNotFoundException, UnsupportedEncodingException {
		HeadData.headchances = new HashMap<String, Double>();
		
		PrintWriter writer = null;
		if (!dir.isDirectory() || !file.isFile()) {
			dir.mkdirs();
			writer = new PrintWriter(dirpath + File.separator + "headchances.txt", "UTF-8");
		}
		else {
			String content = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "headchances.txt", new String[0])));
			String[] cspl = content.replaceAll("\n", "").split(",");
			for (String line : cspl) {
				String[] linespl = line.replace("\"", "").replaceAll(" ", "").trim().split(":");
				if (linespl.length < 2) {
					continue;
				}
				
				String mobname = linespl[0];
				Double chancevalue = 0.0;
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
