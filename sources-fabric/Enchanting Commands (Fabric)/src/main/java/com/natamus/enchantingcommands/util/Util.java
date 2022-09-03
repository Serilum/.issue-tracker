/*
 * This is the latest source code of Enchanting Commands.
 * Minecraft version: 1.19.2, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.enchantingcommands.util;

import net.minecraft.core.Registry;

import java.util.*;

public class Util {
	final static Map<String, Integer> enchantmentMap = new HashMap<String, Integer>() {{
		for (int x = 0; x < Registry.ENCHANTMENT.size(); x++) {
			String rawname = Registry.ENCHANTMENT.byId(x).getDescriptionId().replace("enchantment.", "").replace("minecraft.", "");
			String[] rnspl = rawname.split("\\.");
			String enchantmentname = rnspl[rnspl.length-1];
			put(enchantmentname, x);
		}
	}};

	public static List<String> enchantments() {
		List<String> enchantments = new ArrayList<String>(enchantmentMap.keySet());
		Collections.sort(enchantments);
		return enchantments;
	}

	public static Integer getEnchantmentID(String enchantment) {
		return enchantmentMap.get(enchantment);
	}
}
