/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.19.2, mod version: 2.3.
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

package com.natamus.grindstonesharpertools.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_usesAfterGrinding;
	@Entry public static int usesAfterGrinding = 32;
	@Comment public static Comment RANGE_usesAfterGrinding;

	@Comment public static Comment DESC_sharpenedDamageModifier;
	@Entry public static double sharpenedDamageModifier = 1.5;
	@Comment public static Comment RANGE_sharpenedDamageModifier;

	@Comment public static Comment DESC_infiniteCreativeUses;
	@Entry public static boolean infiniteCreativeUses = false;

	@Comment public static Comment DESC_sendUsesLeftInChat;
	@Entry public static boolean sendUsesLeftInChat = true;

	@Comment public static Comment DESC_showUsesLeftInItemName;
	@Entry public static boolean showUsesLeftInItemName = true;

	@Comment public static Comment DESC_nameUsesPrefix;
	@Entry public static String nameUsesPrefix = "(Sharpened uses: ";

	@Comment public static Comment DESC_nameUsesSuffix;
	@Entry public static String nameUsesSuffix = ")";
}