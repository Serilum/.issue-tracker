/*
 * This is the latest source code of Healing Soup.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.healingsoup.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> soupHalfHeartHealAmount;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			soupHalfHeartHealAmount = builder
					.comment("The amount of half hearts the mushroom stew/soup should heal. Minecraft PvP's default was 8.")
					.defineInRange("soupHalfHeartHealAmount", 8, 0, 20);
			
			builder.pop();
		}
	}
}