/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.naturallychargedcreepers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NaturallyChargedCreepersConfigHandler {
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