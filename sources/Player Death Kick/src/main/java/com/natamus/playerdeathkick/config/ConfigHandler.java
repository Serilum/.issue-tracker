/*
 * This is the latest source code of Player Death Kick.
 * Minecraft version: 1.19.3, mod version: 1.8.
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

package com.natamus.playerdeathkick.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<String> disconnectMessage;
		public final ForgeConfigSpec.ConfigValue<Boolean> addDeathCauseToMessage;
		public final ForgeConfigSpec.ConfigValue<Boolean> exemptAdminPlayers;
		public final ForgeConfigSpec.ConfigValue<Boolean> broadcastKickToServer;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			disconnectMessage = builder
					.comment("The message players will receive when disconnected on death.")
					.define("disconnectMessage", "You died by %death%! You have been disconnected from the server.");
			addDeathCauseToMessage = builder
					.comment("If enabled, replaces %death% in the disconnect message with the death cause.")
					.define("addDeathCauseToMessage", true);
			exemptAdminPlayers = builder
					.comment("If enabled, exempts admin players (with cheat access, OPs) from being kicked on death.")
					.define("exemptAdminPlayers", true);
			broadcastKickToServer = builder
					.comment("If enabled, sends a message to all players online with who was kicked.")
					.define("broadcastKickToServer", true);
			
			
			builder.pop();
		}
	}
}