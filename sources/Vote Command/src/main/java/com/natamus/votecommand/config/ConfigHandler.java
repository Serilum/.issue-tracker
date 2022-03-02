/*
 * This is the latest source code of Vote Command.
 * Minecraft version: 1.18.2, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Vote Command ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.votecommand.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<String> voteCommandMessage;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			voteCommandMessage = builder
					.comment("The message which will be sent to players when they use the /vote command.")
					.define("voteCommandMessage", "This is an example /vote message. You can change it in the config.");
			
			builder.pop();
		}
	}
}