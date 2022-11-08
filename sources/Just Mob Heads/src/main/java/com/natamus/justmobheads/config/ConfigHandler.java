/*
 * This is the latest source code of Just Mob Heads.
 * Minecraft version: 1.19.2, mod version: 6.2.
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

package com.natamus.justmobheads.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> mobSpecificDropChances;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableStandardHeads;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableLootingEnchant;
		public final ForgeConfigSpec.ConfigValue<Boolean> onlyAdultMobsDropTheirHead;
		
		public final ForgeConfigSpec.ConfigValue<Double> overallDropChance;
		public final ForgeConfigSpec.ConfigValue<Double> creeperSkeletonZombieDropChance;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> onlyDropHeadsByChargedCreeper;
		public final ForgeConfigSpec.ConfigValue<Boolean> onlyDropHeadsByPlayerKill;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mobSpecificDropChances = builder
					.comment("If enabled, overrides the 'overallDropChance' variable with the specific values.")
					.define("mobSpecificDropChances", true);
			enableStandardHeads = builder
					.comment("If enabled, allows Creepers, Skeletons and Zombies to drop their heads.")
					.define("enableStandardHeads", false);
			enableLootingEnchant = builder
					.comment("If enabled, the looting enchant will have an effect on the drop chance.")
					.define("enableLootingEnchant", true);
			onlyAdultMobsDropTheirHead = builder
					.comment("If enabled, only adult tameable mobs will have a chance to drop their head on death.")
					.define("onlyAdultMobsDropTheirHead", true);
			
			overallDropChance = builder
					.comment("Sets the chance of a mob dropping its head if 'mobSpecificDropChances' is disabled.")
					.defineInRange("overallDropChance", 0.1, 0.0001, 1.0);
			creeperSkeletonZombieDropChance = builder
					.comment("Sets head drop chance for Zombies, Skeletons and Creepers if 'enableStandardHeads' is enabled.")
					.defineInRange("creeperSkeletonZombieDropChance", 0.1, 0.0001, 1.0 );
			
			onlyDropHeadsByChargedCreeper = builder
					.comment("When enabled, only drops mob heads if the source on death is a charged creeper. This overwrites the onlyDropHeadsByPlayerKill value.")
					.define("onlyDropHeadsByChargedCreeper", false);
			onlyDropHeadsByPlayerKill = builder
					.comment("When enabled, only drops mob heads if the source on death is from a player.")
					.define("onlyDropHeadsByPlayerKill", false);
			
			builder.pop();
		}
	}
}
