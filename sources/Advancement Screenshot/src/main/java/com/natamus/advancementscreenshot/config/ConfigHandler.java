/*
 * This is the latest source code of Advancement Screenshot.
 * Minecraft version: 1.19.1, mod version: 3.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.advancementscreenshot.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> showScreenshotTakenMessage;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			showScreenshotTakenMessage = builder
					.comment("If enabled, shows the normal screenshot taken (same as F2) message when a screenshot is taken after an advancement.")
					.define("showScreenshotTakenMessage", false);
			
			builder.pop();
		}
	}
}