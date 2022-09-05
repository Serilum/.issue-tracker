/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.51.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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
