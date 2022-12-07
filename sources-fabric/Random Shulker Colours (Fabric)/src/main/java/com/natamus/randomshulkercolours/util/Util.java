/*
 * This is the latest source code of Random Shulker Colours.
 * Minecraft version: 1.19.3, mod version: 2.1.
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

package com.natamus.randomshulkercolours.util;

import java.util.ArrayList;
import java.util.List;

import com.natamus.randomshulkercolours.config.ConfigHandler;

import net.minecraft.world.item.DyeColor;

public class Util {
	public static List<DyeColor> possibleColours = null;
	
	public static void initColours() {
		possibleColours = new ArrayList<DyeColor>();
		
		String possiblecolours = ConfigHandler.possibleShulkerColours;
		String[] pcspl = possiblecolours.toLowerCase().replace(" ",  "").split(",");
		for (String pc : pcspl) {
			DyeColor colour = getColourFromString(pc.trim());
			if (colour != null || pc.equalsIgnoreCase("normal")) {
				possibleColours.add(colour);
			}
		}
	}
	
	private static DyeColor getColourFromString(String cs) {
		DyeColor rc = null;
		
		if (cs.equals("black")) {
			rc = DyeColor.BLACK;
		}
		else if (cs.equals("blue")) {
			rc = DyeColor.BLUE;
		}
		else if (cs.equals("brown")) {
			rc = DyeColor.BROWN;
		}
		else if (cs.equals("cyan")) {
			rc = DyeColor.CYAN;
		}
		else if (cs.equals("gray")) {
			rc = DyeColor.GRAY;
		}
		else if (cs.equals("green")) {
			rc = DyeColor.GREEN;
		}
		else if (cs.equals("light_blue")) {
			rc = DyeColor.LIGHT_BLUE;
		}
		else if (cs.equals("light_gray")) {
			rc = DyeColor.LIGHT_GRAY;
		}
		else if (cs.equals("lime")) {
			rc = DyeColor.LIME;
		}
		else if (cs.equals("magenta")) {
			rc = DyeColor.MAGENTA;
		}
		else if (cs.equals("orange")) {
			rc = DyeColor.ORANGE;
		}
		else if (cs.equals("pink")) {
			rc = DyeColor.PINK;
		}
		else if (cs.equals("purple")) {
			rc = DyeColor.PURPLE;
		}
		else if (cs.equals("red")) {
			rc = DyeColor.RED;
		}
		else if (cs.equals("white")) {
			rc = DyeColor.WHITE;
		}
		else if (cs.equals("yellow")) {
			rc = DyeColor.YELLOW;
		}
		
		return rc;
	}
}
