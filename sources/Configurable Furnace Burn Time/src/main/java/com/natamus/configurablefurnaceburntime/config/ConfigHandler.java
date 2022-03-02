/*
 * This is the latest source code of Configurable Furnace Burn Time.
 * Minecraft version: 1.18.2, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Configurable Furnace Burn Time ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.configurablefurnaceburntime.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Double> burnTimeModifier;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			burnTimeModifier = builder
					.comment("How much the fuel burn time should be modified. It's calculated by default burn time * burnTimeModifier. For example: sticks are by default 100 ticks (5 seconds). A burnTimeModifier of 2.0 makes it 200 ticks (10 seconds).")
					.defineInRange("burnTimeModifier", 1.0, 0.0, 1000.0);
			
			builder.pop();
		}
	}
}