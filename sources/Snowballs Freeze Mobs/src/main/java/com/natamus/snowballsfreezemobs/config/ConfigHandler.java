/*
 * This is the latest source code of Snowballs Freeze Mobs.
 * Minecraft version: 1.18.0, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Snowballs Freeze Mobs ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.snowballsfreezemobs.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> freezeTimeMs;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			freezeTimeMs = builder
					.comment("The amount of time in ms the mob hit will be frozen for.")
					.defineInRange("freezeTimeMs", 5000, 0, 3600000);
			
			builder.pop();
		}
	}
}