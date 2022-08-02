/*
 * This is the latest source code of Player Death Kick.
 * Minecraft version: 1.19.1, mod version: 1.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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