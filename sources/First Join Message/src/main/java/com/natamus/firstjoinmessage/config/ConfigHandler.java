/*
 * This is the latest source code of First Join Message.
 * Minecraft version: 1.18.1, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of First Join Message ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.firstjoinmessage.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<String> firstJoinMessage;
		public final ForgeConfigSpec.ConfigValue<Integer> firstJoinMessageTextFormattingColourIndex;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			firstJoinMessage = builder
					.comment("The message players will receive when they join a world for the first time.")
					.define("firstJoinMessage", "You wake up in an unfamiliar place.");
			firstJoinMessageTextFormattingColourIndex = builder
					.comment("The colour of the message. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
					.defineInRange("firstJoinMessageTextFormattingColourIndex", 2, 0, 15);
			
			
			builder.pop();
		}
	}
}
