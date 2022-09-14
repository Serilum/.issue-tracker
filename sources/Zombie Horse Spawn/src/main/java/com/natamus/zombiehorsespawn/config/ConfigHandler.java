/*
 * This is the latest source code of Zombie Horse Spawn.
 * Minecraft version: 1.19.2, mod version: 3.2.
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

package com.natamus.zombiehorsespawn.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Double> chanceSurfaceZombieHasHorse;
		public final ForgeConfigSpec.ConfigValue<Boolean> shouldBurnZombieHorsesInDaylight;
		public final ForgeConfigSpec.ConfigValue<Boolean> onlySpawnZombieHorsesOnSurface;
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			chanceSurfaceZombieHasHorse = builder
					.comment("The chance a zombie that has spawned on the surface is riding a horse.")
					.defineInRange("chanceSurfaceZombieHasHorse", 0.05, 0, 1.0);
			shouldBurnZombieHorsesInDaylight = builder
					.comment("If enabled, burns zombie horses when daylight shines upon them.")
					.define("shouldBurnZombieHorsesInDaylight", true);
			onlySpawnZombieHorsesOnSurface = builder
					.comment("If enabled, a zombie horse with rider will only spawn on the surface.")
					.define("onlySpawnZombieHorsesOnSurface", true);
			
			builder.pop();
		}
	}
}