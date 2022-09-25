/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.19.2, mod version: 2.7.
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

package com.natamus.cyclepaintings.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<String> ignorePaintingsInCycleResourceLocation;
		public final ForgeConfigSpec.ConfigValue<Boolean> showRegisteredPaintingsDebug;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			ignorePaintingsInCycleResourceLocation = builder
					.comment("Split by a , (comma). The paintings to ignore during the cycle. You can either input an entire mod's prefix (only the part before the : (colon)) or the entire line found via 'showRegisteredPaintingsDebug'.")
					.define("ignorePaintingsInCycleResourceLocation", "infernalexp,");
			showRegisteredPaintingsDebug = builder
					.comment("When enabled, prints all paintings registered to the console. With this you can find which to add to the 'ignorePaintingsInCycleResourceLocationPrefix' config.")
					.define("showRegisteredPaintingsDebug", false);
			
			builder.pop();
		}
	}
}