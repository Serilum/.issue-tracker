/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.replantingcrops.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ReplantingCropsConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHoldHoeForReplanting;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustHoldHoeForReplanting = builder
					.comment("If enabled, players must hold a hoe in their hand to automatically replant the crop.")
					.define("mustHoldHoeForReplanting", true);
			
			builder.pop();
		}
	}
}
