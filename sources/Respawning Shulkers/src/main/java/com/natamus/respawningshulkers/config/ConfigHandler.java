/*
 * This is the latest source code of Respawning Shulkers.
 * Minecraft version: 1.19.1, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.respawningshulkers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> timeInTicksToRespawn;
		public final ForgeConfigSpec.ConfigValue<Boolean> shulkersFromSpawnersDoNotRespawn;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			timeInTicksToRespawn = builder
					.comment("The amount of time in ticks it takes for a shulker to respawn after it died. 20 ticks = 1 second. By default 60 seconds.")
					.defineInRange("timeInTicksToRespawn", 1200, 1, 72000);
			shulkersFromSpawnersDoNotRespawn = builder
					.comment("If enabled, shulkers which spawn from (modded) spawners will not be respawned after a death.")
					.define("shulkersFromSpawnersDoNotRespawn", true);
			
			builder.pop();
		}
	}
}