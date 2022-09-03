/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.fishontheline.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class FishOnTheLineConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHoldBellInOffhand;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustHoldBellInOffhand = builder
					.comment("If enabled, the fish on the line sound will only go off if a bell is held in the offhand while fishing.")
					.define("mustHoldBellInOffhand", true);
			
			builder.pop();
		}
	}
}