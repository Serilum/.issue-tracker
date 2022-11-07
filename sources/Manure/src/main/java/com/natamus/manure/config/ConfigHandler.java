/*
 * This is the latest source code of Manure.
 * Minecraft version: 1.19.2, mod version: 1.0.
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

package com.natamus.manure.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> manureDropDelayTicks;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			manureDropDelayTicks = builder
					.comment("How long the delay in ticks is in between loaded animals dropping manure. 20 ticks = 1 second. By default twice a day, every 12000 ticks.")
					.defineInRange("manureDropDelayTicks", 12000, 0, 10000000);
			
			builder.pop();
		}
	}
}