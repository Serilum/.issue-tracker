/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.19.1, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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