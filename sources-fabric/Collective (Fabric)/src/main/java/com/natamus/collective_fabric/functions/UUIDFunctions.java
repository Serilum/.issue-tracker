/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 4.9.
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

package com.natamus.collective_fabric.functions;

import java.util.ArrayList;
import java.util.List;

public class UUIDFunctions {
	public static List<Integer> oldIdToIntArray(String oldid) {
		String oldidfull = oldid.replace("-", "");

		return getIntegerParts(oldidfull, 8);
		//return getConvertedUUID(parts.toArray(new Integer[parts.size()]));
	}
	
	@SuppressWarnings("unused")
	private static String getConvertedUUID(Integer[] ints) {  
		StringBuilder sb = new StringBuilder();
		sb.append("I;");
		for(int x = 0; x < ints.length - 1; x++) {
			sb.append(ints[x]);
			sb.append(",");
		}

		sb.append(ints[ints.length - 1]);

		return sb.toString();
	}
	
    private static List<Integer> getIntegerParts(String string, int partitionSize) {
        List<Integer> parts = new ArrayList<Integer>();
        int len = string.length();
        for (int i=0; i<len; i+=partitionSize) {
            parts.add(partToDecimalValue(string.substring(i, Math.min(len, i + partitionSize))));
        }
        return parts;
    }
    
	private static int partToDecimalValue(String hex) {
		return Long.valueOf(hex, 16).intValue();
	}
}
