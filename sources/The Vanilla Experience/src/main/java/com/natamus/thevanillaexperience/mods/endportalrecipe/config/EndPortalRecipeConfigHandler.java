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

package com.natamus.thevanillaexperience.mods.endportalrecipe.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class EndPortalRecipeConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHaveSilkTouchToBreakPortal;
		public final ForgeConfigSpec.ConfigValue<Boolean> addBrokenPortalFramesToInventory;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustHaveSilkTouchToBreakPortal = builder
					.comment("If enabled, players can only break portal frames if they have silk touch on their pickaxe.")
					.define("mustHaveSilkTouchToBreakPortal", true);
			addBrokenPortalFramesToInventory = builder
					.comment("If enabled, add portal frames directly to the player's inventory to lower the chance of them being destroyed. Still drops the item entity if the inventory is full.")
					.define("addBrokenPortalFramesToInventory", true);
			
			builder.pop();
		}
	}
}
