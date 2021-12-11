/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.18.1, mod version: 2.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Cycle Paintings ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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