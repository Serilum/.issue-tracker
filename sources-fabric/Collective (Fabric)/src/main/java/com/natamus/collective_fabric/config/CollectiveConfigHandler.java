/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.3.
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

package com.natamus.collective_fabric.config;

public class CollectiveConfigHandler extends DuskConfig {
    @Comment public static Comment DESC_transferItemsBetweenReplacedEntities;
    @Entry public static boolean transferItemsBetweenReplacedEntities = true;

    @Comment public static Comment DESC_loopsAmountUsedToGetAllEntityDrops;
    @Entry public static int loopsAmountUsedToGetAllEntityDrops = 100;
    @Comment public static Comment RANGE_loopsAmountUsedToGetAllEntityDrops;

    @Comment public static Comment DESC_findABlockcheckAroundEntitiesDelayMs;
    @Entry public static int findABlockcheckAroundEntitiesDelayMs = 30000;
    @Comment public static Comment RANGE_findABlockcheckAroundEntitiesDelayMs;

    @Comment public static Comment DESC_enableAntiRepostingCheck;
    @Entry public static boolean enableAntiRepostingCheck = true;

    @Comment public static Comment DESC_enablePatronPets;
    @Entry public static boolean enablePatronPets = true;
}