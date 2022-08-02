/*
 * This is the latest source code of Sleep Sooner.
 * Minecraft version: 1.19.1, mod version: 2.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.sleepsooner.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> enableSleepSooner;
		public final ForgeConfigSpec.ConfigValue<Boolean> enablePreSleepMessage;
		public final ForgeConfigSpec.ConfigValue<Integer> whenSleepIsPossibleInTicks;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			enableSleepSooner = builder
					.comment("Allows you to disable/enable the Sleep Sooner mod.")
					.define("_enableSleepSooner", true);
			enablePreSleepMessage = builder
					.comment("Allows you to disable/enable the message you receive before sleeping. You still need to click the bed twice if the current time is below 12540.")
					.define("enablePreSleepMessage", true);
			whenSleepIsPossibleInTicks = builder
					.comment("The default time in ticks when you can sleep is ~12540. The default Sleep Sooner mod value is 12000.")
					.defineInRange("whenSleepIsPossibleInTicks", 12000, 0, 24000);
			
			builder.pop();
		}
	}
}