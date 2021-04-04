/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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