/*
 * This is the latest source code of Bottled Air.
 * Minecraft version: 1.19.3, mod version: 1.6.
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

package com.natamus.bottledair.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> disableWaterConsumptionUnderwater;
		public final ForgeConfigSpec.ConfigValue<Boolean> holdFireTypeItemInOffhandToPreventWaterBottleCreation;
		public final ForgeConfigSpec.ConfigValue<Double> chanceGlassBottleBreaksWithFireTypeInOffhand;
		public final ForgeConfigSpec.ConfigValue<Integer> amountOfAirInBottles;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			disableWaterConsumptionUnderwater = builder
					.comment("If enabled, players will be unable to drink water bottles underwater to prevent getting unlimited oxygen.")
					.define("disableWaterConsumptionUnderwater", true);
			holdFireTypeItemInOffhandToPreventWaterBottleCreation = builder
					.comment("Whether the creation of water bottles should be prevented (evaporated) when holding a fire type block in the offhand.")
					.define("holdFireTypeItemInOffhandToPreventWaterBottleCreation", true);
			chanceGlassBottleBreaksWithFireTypeInOffhand = builder
					.comment("The chance a glass bottle breaks when the item in the offhand evaporates the water, giving back an empty (air) bottle.")
					.defineInRange("chanceGlassBottleBreaksWithFireTypeInOffhand", 0.5, 0, 1.0);
			amountOfAirInBottles = builder
					.comment("The amount of air an empty bottle contains. In vanilla Minecraft, 300 is the maximum air supply.")
					.defineInRange("amountOfAirInBottles", 150, 0, 1000);
			
			builder.pop();
		}
	}
}