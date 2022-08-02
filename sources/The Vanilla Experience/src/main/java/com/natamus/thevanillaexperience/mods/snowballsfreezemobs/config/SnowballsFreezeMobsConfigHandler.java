/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.snowballsfreezemobs.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SnowballsFreezeMobsConfigHandler {
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