/*
 * This is the latest source code of Vote Command.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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