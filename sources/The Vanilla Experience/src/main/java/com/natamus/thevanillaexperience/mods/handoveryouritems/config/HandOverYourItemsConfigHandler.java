/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.handoveryouritems.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class HandOverYourItemsConfigHandler {
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