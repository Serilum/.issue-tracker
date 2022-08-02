/*
 * This is the latest source code of Weaker Spiderwebs.
 * Minecraft version: 1.19.1, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.weakerspiderwebs.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> breakSpiderwebDelay;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			breakSpiderwebDelay = builder
					.comment("The delay in ms after walking in a spiderweb until it breaks.")
					.defineInRange("breakSpiderwebDelay", 500, 0, 10000);
			
			builder.pop();
		}
	}
}