/*
 * This is the latest source code of First Join Message.
 * Minecraft version: 1.19.3, mod version: 1.6.
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
