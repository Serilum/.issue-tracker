/*
 * This is the latest source code of More Zombie Villagers.
 * Minecraft version: 1.19.1, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.morezombievillagers.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Double> zombieIsVillagerChance;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			zombieIsVillagerChance = builder
					.comment("The overridden chance a new zombie spawn is of the villager variant.")
					.defineInRange("zombieIsVillagerChance", 0.25, 0, 1.0);
			
			builder.pop();
		}
	}
}