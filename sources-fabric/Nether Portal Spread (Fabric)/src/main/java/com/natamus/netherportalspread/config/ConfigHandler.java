/*
 * This is the latest source code of Nether Portal Spread.
 * Minecraft version: 1.19.2, mod version: 6.5.
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

package com.natamus.netherportalspread.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_sendMessageOnPortalCreation;
	@Entry public static boolean sendMessageOnPortalCreation = true;

	@Comment public static Comment DESC_messageOnPortalCreation;
	@Entry public static String messageOnPortalCreation = "You feel a corrupted energy coming from the portal. The nether will slowly spread into the overworld unless %preventSpreadBlockAmountNeeded% %preventSpreadBlockString% are placed within a %portalSpreadRadius% block radius around the portal.";

	@Comment public static Comment DESC_sendMessageOnPreventSpreadBlocksFound;
	@Entry public static boolean sendMessageOnPreventSpreadBlocksFound = true;

	@Comment public static Comment DESC_messageOnPreventSpreadBlocksFound;
	@Entry public static String messageOnPreventSpreadBlocksFound = "With enough %preventSpreadBlockString% placed, you feel the corrupted energy fade.";

	@Comment public static Comment DESC_sendMessageOnPortalBroken;
	@Entry public static boolean sendMessageOnPortalBroken = true;

	@Comment public static Comment DESC_messageOnPortalBroken;
	@Entry public static String messageOnPortalBroken = "With the nether portal broken, the corrupted energy is no longer able to enter the overworld.";

	@Comment public static Comment DESC_prefixPortalCoordsInMessage;
	@Entry public static boolean prefixPortalCoordsInMessage = true;

	@Comment public static Comment DESC_portalSpreadRadius;
	@Entry public static int portalSpreadRadius = 30;
	@Comment public static Comment RANGE_portalSpreadRadius;

	@Comment public static Comment DESC_spreadDelayTicks;
	@Entry public static int spreadDelayTicks = 40;
	@Comment public static Comment RANGE_spreadDelayTicks;

	@Comment public static Comment DESC_instantConvertAmount;
	@Entry public static int instantConvertAmount = 50;
	@Comment public static Comment RANGE_instantConvertAmount;

	@Comment public static Comment DESC_preventSpreadWithBlock;
	@Entry public static boolean preventSpreadWithBlock = true;

	@Comment public static Comment DESC_preventSpreadBlockAmountNeeded;
	@Entry public static int preventSpreadBlockAmountNeeded = 4;

	@Comment public static Comment DESC_preventSpreadBlockString;
	@Entry public static String preventSpreadBlockString = "minecraft:coal_block";
}