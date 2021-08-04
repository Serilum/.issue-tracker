/*
 * This is the latest source code of End Portal Recipe.
 * Minecraft version: 1.17.1, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of End Portal Recipe ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
