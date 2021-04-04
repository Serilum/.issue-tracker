/*
 * This is the latest source code of Crying Ghasts.
 * Minecraft version: 1.16.5, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Crying Ghasts ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.cryingghasts.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Integer> maxDistanceToGhastBlocks;
		public final ForgeConfigSpec.ConfigValue<Integer> ghastTearDelayTicks;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			maxDistanceToGhastBlocks = builder
					.comment("The maximum distance in blocks the player can be away from the ghasts in order for them to start dropping tears periodically.")
					.defineInRange("maxDistanceToGhastBlocks", 32, 1, 256);
			ghastTearDelayTicks = builder
					.comment("The delay in between ghasts dropping a tear in ticks (1 second = 20 ticks).")
					.defineInRange("ghastTearDelayTicks", 1200, 1, 72000);
			
			builder.pop();
		}
	}
}