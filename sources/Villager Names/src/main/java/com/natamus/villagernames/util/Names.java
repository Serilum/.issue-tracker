/*
 * This is the latest source code of Villager Names.
 * Minecraft version: 1.18.1, mod version: 3.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Villager Names ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.villagernames.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.villagernames.config.ConfigHandler;

public class Names {
	public static List<String> customnames = null;
	public static String workspace_path = System.getProperty("user.dir");
	
	public static void setCustomNames() throws IOException {
		String dirpath = workspace_path + File.separator + "config" + File.separator + "villagernames";
		File dir = new File(dirpath);
		File file = new File(dirpath + File.separator + "customnames.txt");
		
		if (dir.isDirectory() && file.isFile()) {
			String cn = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "customnames.txt", new String[0])), "UTF-8");
			cn = cn.replace("\n", "").replace("\r", "");
			
			String[] cns = cn.split(",");
			customnames = Arrays.asList(cns);
		}
		else {
			dir.mkdirs();
			
			PrintWriter writer = new PrintWriter(dirpath + File.separator + "customnames.txt", "UTF-8");
			writer.println("Rick,");
			writer.println("Bob,");
			writer.println("Eve");
			writer.close();
			
			customnames = new ArrayList<>(Arrays.asList("Rick", "Bob", "Eve"));
		}
	}
	
	public static String getRandomName() {
		List<String> allnames = null;
		if (ConfigHandler.GENERAL._useFemaleNames.get() && ConfigHandler.GENERAL._useMaleNames.get()) {
			allnames = Stream.concat(GlobalVariables.femalenames.stream(), GlobalVariables.malenames.stream()).collect(Collectors.toList());
		}
		else if (ConfigHandler.GENERAL._useFemaleNames.get()) {
			allnames = GlobalVariables.femalenames;
		}
		else if (ConfigHandler.GENERAL._useMaleNames.get()) {
			allnames = GlobalVariables.malenames;
		}
		
		if (ConfigHandler.GENERAL._useCustomNames.get() && customnames != null) {
			if (allnames == null) {
				allnames = customnames;
			}
			else {
				allnames = Stream.concat(customnames.stream(), allnames.stream()).collect(Collectors.toList());
			}
		}
		
		if (allnames == null) {
			return "";
		}
		
	    String name = allnames.get(GlobalVariables.random.nextInt(allnames.size())).toLowerCase();
	    return StringFunctions.capitalizeEveryWord(name);
	}
}
