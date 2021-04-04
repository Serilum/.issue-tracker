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

package com.natamus.thevanillaexperience.mods.weakerspiderwebs.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class WeakerSpiderwebsConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> breakSpiderwebDelay;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			breakSpiderwebDelay = builder
					.comment("The delay in ms after walking in a spiderweb until it breaks.")
					.defineInRange("breakSpiderwebDelay", 500, 0, 10000);
			
			builder.pop();
		}
	}
}