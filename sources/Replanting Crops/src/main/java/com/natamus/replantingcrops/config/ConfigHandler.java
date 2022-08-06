/*
 * This is the latest source code of Replanting Crops.
 * Minecraft version: 1.19.2, mod version: 2.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.replantingcrops.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHoldHoeForReplanting;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustHoldHoeForReplanting = builder
					.comment("If enabled, players must hold a hoe in their hand to automatically replant the crop.")
					.define("mustHoldHoeForReplanting", true);
			
			builder.pop();
		}
	}
}
