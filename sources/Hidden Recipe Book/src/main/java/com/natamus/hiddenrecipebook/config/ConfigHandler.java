/*
 * This is the latest source code of Hidden Recipe Book.
 * Minecraft version: 1.17.1, mod version: 2.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Hidden Recipe Book ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.hiddenrecipebook.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> shouldHideRecipeBook;
		public final ForgeConfigSpec.ConfigValue<Boolean> allowRecipeBookToggleHotkey;
		public final ForgeConfigSpec.ConfigValue<Boolean> showMessageOnRecipeBookToggle;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			shouldHideRecipeBook = builder
					.comment("If the recipe book should be hidden by default on first launch of the client.")
					.define("shouldHideRecipeBook", true);
			allowRecipeBookToggleHotkey = builder
					.comment("If the recipe book visibility can be toggled with the hotkey.")
					.define("allowRecipeBookToggleHotkey", true);
			showMessageOnRecipeBookToggle = builder
					.comment("If a message should be sent whenever the recipe book is shown/hidden.")
					.define("showMessageOnRecipeBookToggle", false);
			
			builder.pop();
		}
	}
}