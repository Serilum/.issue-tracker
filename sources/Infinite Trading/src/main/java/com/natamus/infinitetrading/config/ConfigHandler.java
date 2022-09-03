/*
 * This is the latest source code of Infinite Trading.
 * Minecraft version: 1.19.2, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.infinitetrading.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> wanderingTraderInfiniteTrades;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			wanderingTraderInfiniteTrades = builder
					.comment("When enabled, the trades of the wandering trader also never lock up.")
					.define("wanderingTraderInfiniteTrades", true);
			
			builder.pop();
		}
	}
}