/*
 * This is the latest source code of Mooshroom Spawn.
 * Minecraft version: 1.19.2, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.mooshroomspawn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Double> chanceCowIsMooshroom;
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			chanceCowIsMooshroom = builder
					.comment("The chance a cow that has spawned is of the mooshroom variant.")
					.defineInRange("chanceCowIsMooshroom", 0.05, 0, 1.0);
			
			builder.pop();
		}
	}
}