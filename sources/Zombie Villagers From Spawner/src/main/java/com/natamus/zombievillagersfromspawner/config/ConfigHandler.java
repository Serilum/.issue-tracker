/*
 * This is the latest source code of Zombie Villagers From Spawner.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.zombievillagersfromspawner.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Double> isZombieVillagerChance;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			isZombieVillagerChance = builder
					.comment("The chance a new zombie spawn from a spawner is of the villager variant.")
					.defineInRange("isZombieVillagerChance", 0.1, 0, 1.0);
			
			builder.pop();
		}
	}
}