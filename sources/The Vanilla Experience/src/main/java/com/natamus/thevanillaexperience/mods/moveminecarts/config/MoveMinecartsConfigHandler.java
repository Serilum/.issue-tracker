/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.moveminecarts.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MoveMinecartsConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> mustSneakToPickUp;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustSneakToPickUp = builder
					.comment("When enabled, will only pick up the minecart when the player is sneaking and holding right-click.")
					.define("mustSneakToPickUp", true);
			
			builder.pop();
		}
	}
}