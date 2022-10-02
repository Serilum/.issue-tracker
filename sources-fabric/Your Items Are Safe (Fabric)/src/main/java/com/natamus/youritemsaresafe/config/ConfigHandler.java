/*
 * This is the latest source code of Your Items Are Safe.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.youritemsaresafe.config;

import com.natamus.collective_fabric.config.DuskConfig;

public class ConfigHandler extends DuskConfig {
	@Comment public static Comment DESC_mustHaveItemsInInventoryForCreation;
	@Entry public static boolean mustHaveItemsInInventoryForCreation = true;

	@Comment public static Comment DESC_addPlayerHeadToArmorStand;
	@Entry public static boolean addPlayerHeadToArmorStand = true;

	@Comment public static Comment DESC_createArmorStand;
	@Entry public static boolean createArmorStand = true;

	@Comment public static Comment DESC_createSignWithPlayerName;
	@Entry public static boolean createSignWithPlayerName = true;

	@Comment public static Comment DESC_needChestMaterials;
	@Entry public static boolean needChestMaterials = true;

	@Comment public static Comment DESC_needArmorStandMaterials;
	@Entry public static boolean needArmorStandMaterials = true;

	@Comment public static Comment DESC_needSignMaterials;
	@Entry public static boolean needSignMaterials = false;

	@Comment public static Comment DESC_ignoreStoneMaterialNeed;
	@Entry public static boolean ignoreStoneMaterialNeed = true;

	@Comment public static Comment DESC_sendMessageOnCreationFailure;
	@Entry public static boolean sendMessageOnCreationFailure = true;

	@Comment public static Comment DESC_sendMessageOnCreationSuccess;
	@Entry public static boolean sendMessageOnCreationSuccess = true;

	@Comment public static Comment DESC_creationFailureMessage;
	@Entry public static String creationFailureMessage = "Your items are not safe due to having insufficient materials. Missing: %plankamount% planks.";

	@Comment public static Comment DESC_creationSuccessMessage;
	@Entry public static String creationSuccessMessage = "Your items are safe at your death location.";
}