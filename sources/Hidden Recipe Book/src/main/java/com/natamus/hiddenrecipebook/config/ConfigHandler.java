/*
 * This is the latest source code of Hidden Recipe Book.
 * Minecraft version: 1.19.2, mod version: 3.0.
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