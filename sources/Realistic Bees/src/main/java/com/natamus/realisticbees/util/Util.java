/*
 * This is the latest source code of Realistic Bees.
 * Minecraft version: 1.18.2, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Realistic Bees ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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
