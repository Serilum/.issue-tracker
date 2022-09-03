/*
 * This is the latest source code of Mooshroom Tweaks.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.mooshroomtweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Double> becomeBrownChance;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			becomeBrownChance = builder
					.comment("The chance of a Red Mooshroom becoming a Brown Mooshroom.")
					.defineInRange("becomeBrownChance", 0.5, 0, 1.0);
			
			builder.pop();
		}
	}
}