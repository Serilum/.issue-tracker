/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.15.
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

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonFunctions {
	public static HashMap<String, String> JsonStringToHashMap(String jsonstring) {
		return new Gson().fromJson(jsonstring, new TypeToken<HashMap<String, String>>(){}.getType());
	}
	
	public static String HashMapToJsonString(HashMap<String, String> map) {
		return new Gson().toJson(map); 
	}
}
