/*
 * This is the latest source code of Stray Spawn.
 * Minecraft version: 1.16.5, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Stray Spawn ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.strayspawn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Double> chanceSkeletonIsStray;
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			chanceSkeletonIsStray = builder
					.comment("The chance a skeleton that has spawned is of the stray variant.")
					.defineInRange("chanceSkeletonIsStray", 0.25, 0, 1.0);
			
			builder.pop();
		}
	}
}