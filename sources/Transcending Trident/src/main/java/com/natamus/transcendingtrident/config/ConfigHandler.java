/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.19.1, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.transcendingtrident.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHoldBucketOfWater;
		public final ForgeConfigSpec.ConfigValue<Integer> tridentUseDuration;
		public final ForgeConfigSpec.ConfigValue<Double> tridentUsePowerModifier;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustHoldBucketOfWater = builder
					.comment("When enabled, Riptide can only be used without rain when the user is holding a bucket of water.")
					.define("mustHoldBucketOfWater", true);
			tridentUseDuration = builder
					.comment("The amount of time a player needs to charge the trident before being able to use Riptide. Minecraft's default is 10.")
					.defineInRange("tridentUseDuration", 5, 0, 20);
			tridentUsePowerModifier = builder
					.comment("The riptide power of the trident is multiplied by this number on use. Allows traveling a greater distance with a single charge.")
					.defineInRange("tridentUsePowerModifier", 3.0, 0, 100.0);
			
			builder.pop();
		}
	}
}