/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.44.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.functions;

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
