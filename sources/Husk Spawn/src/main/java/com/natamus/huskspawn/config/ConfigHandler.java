/*
 * This is the latest source code of Husk Spawn.
 * Minecraft version: 1.19.2, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.huskspawn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Double> chanceZombieIsHusk;
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			chanceZombieIsHusk = builder
					.comment("The chance a zombie that has spawned is of the husk variant.")
					.defineInRange("chanceZombieIsHusk", 0.15, 0, 1.0);
			
			builder.pop();
		}
	}
}