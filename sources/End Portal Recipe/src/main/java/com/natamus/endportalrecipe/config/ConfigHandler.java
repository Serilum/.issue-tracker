/*
 * This is the latest source code of End Portal Recipe.
 * Minecraft version: 1.19.2, mod version: 2.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.endportalrecipe.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
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
