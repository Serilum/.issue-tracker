/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.pumpkillagersquest.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Double> pumpkillagerSpawnChance;
		public final ForgeConfigSpec.ConfigValue<Boolean> enablePumpkillagerSpawnDuringCreative;
		public final ForgeConfigSpec.ConfigValue<Double> finalBossMaxHealth;
		public final ForgeConfigSpec.ConfigValue<Integer> experienceAmountRewardFinalBoss;
		public final ForgeConfigSpec.ConfigValue<Double> chanceForPumpkinBlockToSpawnOnPigSpawn;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			pumpkillagerSpawnChance = builder
					.comment("The chance a Pumpkillager will spawn when a player breaks a pumpkin block.")
					.defineInRange("pumpkillagerSpawnChance", 0.1, 0.001, 1.0);
			enablePumpkillagerSpawnDuringCreative = builder
					.comment("Whether the Pumpkillager should spawn when pumpkins are broken while the player is in creative mode.")
					.define("enablePumpkillagerSpawnDuringCreative", false);
			finalBossMaxHealth = builder
					.comment("The amount of health the final boss should have.")
					.defineInRange("finalBossMaxHealth", 300.0, 1.0, 10000.0);
			experienceAmountRewardFinalBoss = builder
					.comment("How much experience the final boss should drop on death.")
					.defineInRange("experienceAmountRewardFinalBoss", 250, 0, 10000);
			chanceForPumpkinBlockToSpawnOnPigSpawn = builder
					.comment("This is a feature to add more pumpkins to the world, to make people come across the Pumpkillager in a more natural way. Whenever a pig spawns, it has a chance to spawn on top of a pumpkin block. This will make them be sprinkled around the world. To turn it off, set the config chance value to 0.")
					.defineInRange("chanceForPumpkinBlockToSpawnOnPigSpawn", 0.15, 0.0, 1.0);
			
			builder.pop();
		}
	}
}