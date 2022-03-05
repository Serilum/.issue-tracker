/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.2, mod version: 4.22.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

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
