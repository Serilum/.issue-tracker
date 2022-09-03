/*
 * This is the latest source code of Snowballs Freeze Mobs.
 * Minecraft version: 1.19.2, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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