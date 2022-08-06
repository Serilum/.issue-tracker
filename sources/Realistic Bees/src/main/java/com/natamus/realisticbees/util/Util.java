/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.19.2, mod version: 2.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
