/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.36.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
