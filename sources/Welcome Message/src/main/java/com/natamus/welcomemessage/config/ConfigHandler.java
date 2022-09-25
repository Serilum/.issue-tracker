/*
 * This is the latest source code of Welcome Message.
 * Minecraft version: 1.19.2, mod version: 1.7.
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

package com.natamus.welcomemessage.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> onlyRunOnDedicatedServers;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendEmptyLineBeforeFirstMessage;
		
		public final ForgeConfigSpec.ConfigValue<String> messageOneText;
		public final ForgeConfigSpec.ConfigValue<Integer> messageOneColourIndex;
		public final ForgeConfigSpec.ConfigValue<String> messageOneOptionalURL;
		
		public final ForgeConfigSpec.ConfigValue<String> messageTwoText;
		public final ForgeConfigSpec.ConfigValue<Integer> messageTwoColourIndex;
		public final ForgeConfigSpec.ConfigValue<String> messageTwoOptionalURL;
		
		public final ForgeConfigSpec.ConfigValue<String> messageThreeText;
		public final ForgeConfigSpec.ConfigValue<Integer> messageThreeColourIndex;
		public final ForgeConfigSpec.ConfigValue<String> messageThreeOptionalURL;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			onlyRunOnDedicatedServers = builder
					.comment("If the mod should only run on dedicated servers. When enabled it's not sent when in a singleplayer world.")
					.define("onlyRunOnDedicatedServers", true);
			sendEmptyLineBeforeFirstMessage = builder
					.comment("Whether an empty line should be send before to first message to separate the welcome from other chat messages that might be sent.")
					.define("sendEmptyLineBeforeFirstMessage", true);
			
			messageOneText = builder
					.comment("The first message a player will receive when joining the world. Can be left empty.")
					.define("messageOneText", "Welcome to the server!");
			messageOneColourIndex = builder
					.comment("0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white")
					.defineInRange("messageOneColourIndex", 2, 0, 15);
			messageOneOptionalURL = builder
					.comment("If a link is entered here, the complete message will be clickable.")
					.define("messageOneOptionalURL", "");
			
			messageTwoText = builder
					.comment("The second message a player will receive when joining the world. Can be left empty.")
					.define("messageTwoText", "Downloaded from CurseForge! This is a clickable link.");
			messageTwoColourIndex = builder
					.comment("0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white")
					.defineInRange("messageTwoColourIndex", 14, 0, 15);
			messageTwoOptionalURL = builder
					.comment("If a link is entered here, the complete message will be clickable.")
					.define("messageTwoOptionalURL", "https://curseforge.com/members/serilum/projects");
			
			messageThreeText = builder
					.comment("The third message a player will receive when joining the world. Can be left empty.")
					.define("messageThreeText", "You should probably edit this in the config :)");
			messageThreeColourIndex = builder
					.comment("0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white")
					.defineInRange("messageThreeColourIndex", 15, 0, 15);
			messageThreeOptionalURL = builder
					.comment("If a link is entered here, the complete message will be clickable.")
					.define("messageThreeOptionalURL", "");
			
			
			builder.pop();
		}
	}
}
