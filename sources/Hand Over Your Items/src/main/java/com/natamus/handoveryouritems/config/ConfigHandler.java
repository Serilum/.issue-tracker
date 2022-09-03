/*
 * This is the latest source code of Hand Over Your Items.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
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