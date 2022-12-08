/*
 * This is the latest source code of Fire Spread Tweaks.
 * Minecraft version: 1.19.3, mod version: 1.5.
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

package com.natamus.firespreadtweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> timeFireBurnsInTicks;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> enableRandomizedFireDuration;
		public final ForgeConfigSpec.ConfigValue<Integer> MinRandomExtraBurnTicks;
		public final ForgeConfigSpec.ConfigValue<Integer> MaxRandomExtraBurnTicks;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			timeFireBurnsInTicks = builder
					.comment("The time a fire will keep burning for in ticks. 20 ticks = 1 second.")
					.defineInRange("timeFireBurnsInTicks", 300, 0, 72000);
			
			enableRandomizedFireDuration = builder
					.comment("When enabled, uses the MinRandomExtraBurnTicks and MaxRandomExtraBurnTicks config values to randomize the time fire burns for.")
					.define("enableRandomizedFireDuration", true);	
			MinRandomExtraBurnTicks = builder
					.comment("If randomized fire duration is enabled, a random tick number will be chosen in between the minimum and maximum value. This will be added to timeFireBurnsInTicks. If the outcome is negative, it will be subtracted.")
					.defineInRange("MinRandomExtraBurnTicks", -100, -36000, 0);
			MaxRandomExtraBurnTicks = builder
					.comment("See MinRandomExtraBurnTicks's description.")
					.defineInRange("MaxRandomExtraBurnTicks", 100, 0, 36000);
			
			builder.pop();
		}
	}
}