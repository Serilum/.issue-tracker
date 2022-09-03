/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.extractpoison.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ExtractPoisonConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> extractDelayMs;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			extractDelayMs = builder
					.comment("The delay in ms in between the ability to extract poison per mob.")
					.defineInRange("extractDelayMs", 60000, 0, 3600000);
			
			builder.pop();
		}
	}
}