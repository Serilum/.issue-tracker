/*
 * This is the latest source code of Hand Over Your Items.
 * Minecraft version: 1.16.5, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Hand Over Your Items ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.handoveryouritems.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> sendItemReceivedMessage;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendItemGivenMessage;
		public final ForgeConfigSpec.ConfigValue<Boolean> mustCrouchToGiveItem;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			sendItemReceivedMessage = builder
					.comment("If enabled, players will receive a message when they have been given an item.")
					.define("sendItemReceivedMessage", true);
			sendItemGivenMessage = builder
					.comment("If enabled, players will receive a message when they give an item.")
					.define("sendItemGivenMessage", true);
			mustCrouchToGiveItem = builder
					.comment("If enabled, players will only be able to give items when they are crouching/sneaking.")
					.define("mustCrouchToGiveItem", true);
			
			builder.pop();
		}
	}
}