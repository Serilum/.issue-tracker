/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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