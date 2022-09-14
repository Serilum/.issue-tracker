/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.advancementscreenshot.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AdvancementScreenshotConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> showScreenshotTakenMessage;
		public final ForgeConfigSpec.ConfigValue<Boolean> countNewRecipeAdvancements;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			showScreenshotTakenMessage = builder
					.comment("If enabled, shows the normal screenshot taken (same as F2) message when a screenshot is taken after an advancement.")
					.define("showScreenshotTakenMessage", false);
			countNewRecipeAdvancements = builder
					.comment("If enabled, also takes screenshots when new recipes are unlocked.")
					.define("countNewRecipeAdvancements", true);
			
			builder.pop();
		}
	}
}