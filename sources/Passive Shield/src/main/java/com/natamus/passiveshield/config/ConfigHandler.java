/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.16.5, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Passive Shield ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.passiveshield.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> hideShieldWhenNotInUse;
		public final ForgeConfigSpec.ConfigValue<Double> passiveShieldPercentageDamageNegated;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			hideShieldWhenNotInUse = builder
					.comment("When enabled, the shield will be hidden unless a player presses right-click.")
					.define("hideShieldWhenNotInUse", true);
			passiveShieldPercentageDamageNegated = builder
					.comment("The percentage of damage that will be negated when a player is hit while holding a shield that's not held high.")
					.defineInRange("passiveShieldPercentageDamageNegated", 0.3333, 0.0, 1.0);
			
			builder.pop();
		}
	}
}