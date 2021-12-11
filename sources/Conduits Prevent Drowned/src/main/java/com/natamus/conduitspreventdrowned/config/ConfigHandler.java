/*
 * This is the latest source code of Conduits Prevent Drowned.
 * Minecraft version: 1.18.1, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Conduits Prevent Drowned ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.conduitspreventdrowned.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> preventDrownedInRange;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			preventDrownedInRange = builder
					.comment("The euclidian distance range around the drowned where a check for a player with the conduit effect is done. A value of 400 prevents the spawning of all drowned around.")
					.defineInRange("preventDrownedInRange", 400, 0, 400);
			
			builder.pop();
		}
	}
}