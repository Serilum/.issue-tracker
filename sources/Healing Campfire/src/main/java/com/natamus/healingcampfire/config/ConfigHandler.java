/*
 * This is the latest source code of Healing Campfire.
 * Minecraft version: 1.19.2, mod version: 3.9.
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

package com.natamus.healingcampfire.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> checkForCampfireDelayInTicks;
		public final ForgeConfigSpec.ConfigValue<Integer> healingRadius;
		public final ForgeConfigSpec.ConfigValue<Integer> effectDurationSeconds;
		public final ForgeConfigSpec.ConfigValue<Integer> regenerationLevel;
		public final ForgeConfigSpec.ConfigValue<Boolean> healPassiveMobs;
		public final ForgeConfigSpec.ConfigValue<Boolean> hideEffectParticles;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> campfireMustBeLit;
		public final ForgeConfigSpec.ConfigValue<Boolean> campfireMustBeSignalling;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> enableEffectForNormalCampfires;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableEffectForSoulCampfires;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			checkForCampfireDelayInTicks = builder
					.comment("How often in ticks the mod checks for campfires around the player. 1 second = 20 ticks, so by default every 2 seconds.")
					.defineInRange("checkForCampfireDelayInTicks", 40, 1, 1200);
			healingRadius = builder
					.comment("The radius around the campfire in blocks where players receive the regeneration effect.")
					.defineInRange("healingRadius", 16, 1, 64);
			effectDurationSeconds = builder
					.comment("The duration of the regeneration effect which the campfire applies.")
					.defineInRange("effectDurationSeconds", 60, 1, 600);
			regenerationLevel = builder
					.comment("The level of regeneration which the campfire applies, by default 1.")
					.defineInRange("regenerationLevel", 1, 1, 50);
			
			healPassiveMobs = builder
					.comment("When enabled, the campfire heals passive mobs around where the radius is half the width of a bounding box.")
					.define("healPassiveMobs", true);
			hideEffectParticles = builder
					.comment("When enabled, hides the particles from the regeneration effect around the campfire.")
					.define("hideEffectParticles", true);
			
			campfireMustBeLit = builder
					.comment("When enabled, the campfire only has an effect when the block is lit up.")
					.define("campfireMustBeLit", true);
			campfireMustBeSignalling = builder
					.comment("When enabled, the campfire only has an effect when the block is signalling.")
					.define("campfireMustBeSignalling", false);
			
			enableEffectForNormalCampfires = builder
					.comment("When enabled, the mod will work with normal campfires.")
					.define("enableEffectForNormalCampfires", true);
			enableEffectForSoulCampfires = builder
					.comment("When enabled, the mod will work with soul campfires.")
					.define("enableEffectForSoulCampfires", true);
			
			builder.pop();
		}
	}
}