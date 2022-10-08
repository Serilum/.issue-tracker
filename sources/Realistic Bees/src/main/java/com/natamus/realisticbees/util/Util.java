/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.2, mod version: 2.8.
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

package com.natamus.realisticbees.util;

import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.collective.functions.NumberFunctions;

public class Util {
    public static boolean getPreventBeeSuffocationDamage() {
        return ConfigFunctions.getDictValues(Reference.MOD_ID).get("preventBeeSuffocationDamage").equals("true");
    }

    public static int getBeehiveBeeSpace() {
        String beehiveBeeSpaceString = ConfigFunctions.getDictValues(Reference.MOD_ID).get("beeHiveBeeSpace");
        if (NumberFunctions.isNumeric(beehiveBeeSpaceString)) {
            return Integer.parseInt(beehiveBeeSpaceString);
        }
        return 3;
    }
}
