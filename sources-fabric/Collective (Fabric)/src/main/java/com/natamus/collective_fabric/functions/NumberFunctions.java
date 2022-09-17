/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.63.
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
