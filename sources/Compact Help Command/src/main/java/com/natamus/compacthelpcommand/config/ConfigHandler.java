/*
 * This is the latest source code of Compact Help Command.
 * Minecraft version: 1.18.1, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Compact Help Command ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.compacthelpcommand.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> addVerticalBarSpacing;
		public final ForgeConfigSpec.ConfigValue<Integer> amountCommandsPerPage;
		
		public final ForgeConfigSpec.ConfigValue<Integer> commandColour;
		public final ForgeConfigSpec.ConfigValue<Integer> subcommandColour;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			addVerticalBarSpacing = builder
					.comment("When enabled, adds a space in front and behind a vertical bar in the subcommands.")
					.define("addVerticalBarSpacing", true);
			amountCommandsPerPage = builder
					.comment("The message which will be sent to players when they use the /vote command.")
					.defineInRange("amountCommandsPerPage", 8, 1, 50);
			
			commandColour = builder
					.comment("The colour of the command in /help. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
					.defineInRange("commandColour", 2, 0, 15);
			subcommandColour = builder
					.comment("The colour of the subcommand in /help. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
					.defineInRange("subcommandColour", 7, 0, 15);
			
			builder.pop();
		}
	}
}