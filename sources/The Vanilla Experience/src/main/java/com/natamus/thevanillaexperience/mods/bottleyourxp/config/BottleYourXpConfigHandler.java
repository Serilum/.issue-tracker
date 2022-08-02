/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.bottleyourxp.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BottleYourXpConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> damageOnCreation;
		public final ForgeConfigSpec.ConfigValue<Integer> halfHeartDamageAmount;
		public final ForgeConfigSpec.ConfigValue<Integer> rawXpConsumedOnCreation;


		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			damageOnCreation = builder
					.comment("If enabled, damages the user whenever they create an experience bottle.")
					.define("damageOnCreation", true);
			halfHeartDamageAmount = builder
					.comment("The amount of damage the user takes times half a heart when creating an experience bottle.")
					.defineInRange("halfHeartDamageAmount", 1, 1, 20);
			rawXpConsumedOnCreation = builder
					.comment("The amount of raw xp it takes to produce an experience bottle. Be careful when setting this too low, as it may enable xp duplication.")
					.defineInRange("rawXpConsumedOnCreation", 10, 0, 100);
			
			builder.pop();
		}
	}
}