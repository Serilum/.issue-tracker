/*
 * This is the latest source code of Naturally Charged Creepers.
 * Minecraft version: 1.19.2, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.naturallychargedcreepers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Double> isChargedChance;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			isChargedChance = builder
					.comment("The chance of a creeper being naturally charged.")
					.defineInRange("isChargedChance", 0.1, 0, 1.0);
			
			builder.pop();
		}
	}
}