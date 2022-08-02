/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.passiveshield.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PassiveShieldConfigHandler {
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