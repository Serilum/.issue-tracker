/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.0, mod version: 4.28.
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

import java.util.concurrent.ThreadLocalRandom;

public class NumberFunctions {
	public static int getEnchantingTableLevel(int tablerow, int bookshelfcount) {
		if (bookshelfcount > 15) {
			bookshelfcount = 15;
		}
		
		double base = (ThreadLocalRandom.current().nextInt(1, 8 + 1) + Math.floor(bookshelfcount / 2) + ThreadLocalRandom.current().nextInt(0, bookshelfcount + 1));
		if (tablerow == 0) {
			return (int) Math.max(base/3, 1);
		}
		else if (tablerow == 1) {
			return (int) ((base*2) / 3 + 1);
		}
		else if (tablerow == 2) {
			return (int) Math.max(base, bookshelfcount * 2);
		}
		
		return -1;
	}
	
	public static int moveToZero(int num) {
		if (num > 0) {
			return -1;
		}
		return +1;
	}
	
	public static boolean isNumeric(String string) {
		if (string == null) {
			return false;
		}

		try {
			Double.parseDouble(string);
		}
		catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}
}
