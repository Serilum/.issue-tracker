/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.justplayerheads.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class JustPlayerHeadsConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> enablePlayerHeadCaching;
		public final ForgeConfigSpec.ConfigValue<Boolean> playerDropsHeadOnDeath;
		public final ForgeConfigSpec.ConfigValue<Double> playerHeadDropChance;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			enablePlayerHeadCaching = builder
					.comment("If enabled, enables the caching of player head textures. This fixes the limitations of the Mojang API, but does mean that whenever players update their skin the old head is still dropped until the server reboots or an administator uses the '/jph cache' command.")
					.define("enablePlayerHeadCaching", true);
			playerDropsHeadOnDeath = builder
					.comment("If enabled, allows players to drop their head on death.")
					.define("playerDropsHeadOnDeath", true);
			playerHeadDropChance = builder
					.comment("Sets the chance of a player dropping their head if 'playerDropsHeadOnDeath' is enabled.")
					.defineInRange("playerHeadDropChance", 1.0, 0.0001, 1.0);
			
			builder.pop();
		}
	}
}