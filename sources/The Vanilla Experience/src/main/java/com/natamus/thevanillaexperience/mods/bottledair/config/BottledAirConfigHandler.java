/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.bottledair.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BottledAirConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> holdFireTypeItemInOffhandToPreventWaterBottleCreation;
		public final ForgeConfigSpec.ConfigValue<Double> chanceGlassBottleBreaksWithFireTypeInOffhand;
		public final ForgeConfigSpec.ConfigValue<Integer> amountOfAirInBottles;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
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