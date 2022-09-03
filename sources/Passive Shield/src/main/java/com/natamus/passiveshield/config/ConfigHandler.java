/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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