/*
 * This is the latest source code of Beautified Chat Client.
 * Minecraft version: 1.19.3, mod version: 1.2.
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

package com.natamus.beautifiedchatclient.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<String> chatMessageFormat;
		public final ForgeConfigSpec.ConfigValue<String> timestampFormat;
		
		public final ForgeConfigSpec.ConfigValue<Integer> chatTimestampColour;
		public final ForgeConfigSpec.ConfigValue<Integer> chatUsernameColour;
		public final ForgeConfigSpec.ConfigValue<Integer> chatMessageColour;
		public final ForgeConfigSpec.ConfigValue<Integer> chatOtherSymbolsColour;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			chatMessageFormat = builder
					.comment("Variables: %timestamp% = timestamp set in timestampFormat. %username% = the username of the player who sent the message. %username% = the username of who sent the message.")
					.define("chatMessageFormat", "%timestamp% | %username%: %chatmessage%");
			timestampFormat = builder
					.comment("Example time in formats: 5 seconds past 9 o' clock in the evening. *=Default. *(HH:mm) = 21:00, (HH:mm:ss) = 21:00:05, (hh:mm a) = 9:00 PM")
					.define("timestampFormat", "HH:mm");
			
			chatTimestampColour = builder
					.comment("The colour of the timestamp in the chat message. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
					.defineInRange("chatTimestampColour", 8, 0, 15);
			chatUsernameColour = builder
					.comment("The colour of the username in the chat messsage. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
					.defineInRange("chatUsernameColour", 2, 0, 15);
			chatMessageColour = builder
					.comment("The colour of the chat message. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
					.defineInRange("chatMessageColour", 15, 0, 15);
			chatOtherSymbolsColour = builder
					.comment("The colour of the other symbols from chatMessageFormat. So everything except the variables. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
					.defineInRange("chatOtherSymbolsColour", 7, 0, 15);
			
			builder.pop();
		}
	}
}