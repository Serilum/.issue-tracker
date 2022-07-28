/*
 * This is the latest source code of Hide Hands.
 * Minecraft version: 1.19.1, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Hide Hands ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.hidehands.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> alwaysHideMainHand;
		public final ForgeConfigSpec.ConfigValue<String> hideMainHandWithItems;
		public final ForgeConfigSpec.ConfigValue<Boolean> alwaysHideOffhand;
		public final ForgeConfigSpec.ConfigValue<String> hideOffhandWithItems;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			alwaysHideMainHand = builder
					.comment("If enabled, always hides the main hand. If disabled, hides the main hand when holding the items defined in hideMainHandWithItems.")
					.define("alwaysHideMainHand", false);
			hideMainHandWithItems = builder
					.comment("The items which when held will hide the main hand if alwaysHideMainHand is disabled.")
					.define("hideMainHandWithItems", "");
			
			alwaysHideOffhand = builder
					.comment("If enabled, always hides the offhand. If disabled, hides the offhand when holding the items defined in hideOffhandWithItems.")
					.define("alwaysHideOffhand", false);
			hideOffhandWithItems = builder
					.comment("The items which when held will hide the offhand if alwaysHideOffhand is disabled.")
					.define("hideOffhandWithItems", "minecraft:totem_of_undying,minecraft:torch");
			
			builder.pop();
		}
	}
}