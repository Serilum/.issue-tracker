/*
 * This is the latest source code of Enchanting Commands.
 * Minecraft version: 1.17.1, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Enchanting Commands ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.enchantingcommands.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
	public static List<String> enchantments() {
		List<String> enchants = Arrays.asList("aqua_affinity", "bane_of_arthropods", "blast_protection", "channeling", "curse_of_binding", "curse_of_vanishing", "depth_strider", "efficiency", "feather_falling", "fire_aspect", "fire_protection", "flame", "fortune", "frost_walker", "impaling", "infinity", "knockback", "looting", "loyalty", "luck_of_the_sea", "lure", "mending", "multishot", "piercing", "power", "projectile_protection", "protection", "punch", "quick_charge", "respiration", "riptide", "sharpness", "silk_touch", "smite", "soul_speed", "sweeping_edge", "thorns", "unbreaking");
		return enchants;
	}
	
	@SuppressWarnings("serial")
	public static Integer getEnchantmentID(String enchantment) {
		Map<String, Integer> enchantmentMap = new HashMap<String, Integer>() {{
			put("protection", 0);
			put("fire_protection", 1);
			put("feather_falling", 2);
			put("blast_protection", 3);
			put("projectile_protection", 4);
			put("respiration", 5);
			put("aqua_affinity", 6);
			put("thorns", 7);
			put("depth_strider", 8);
			put("frost_walker", 9);
			put("curse_of_binding", 10);
			put("soul_speed", 11);
			put("sharpness", 12);
			put("smite", 13);
			put("bane_of_arthropods", 14);
			put("knockback", 15);
			put("fire_aspect", 16);
			put("looting", 17);
			put("sweeping_edge", 18);
			put("efficiency", 19);
			put("silk_touch", 20);
			put("unbreaking", 21);
			put("fortune", 22);
			put("power", 23);
			put("punch", 24);
			put("flame", 25);
			put("infinity", 26);
			put("luck_of_the_sea", 27);
			put("lure", 28);
			put("loyalty", 29);
			put("impaling", 30);
			put("riptide", 31);
			put("channeling", 32);
			put("multishot", 33);
			put("quick_charge", 34);
			put("piercing", 35);
			put("mending", 36);
			put("curse_of_vanishing", 37);
			
	    }};
		return enchantmentMap.get(enchantment);
	}
}
