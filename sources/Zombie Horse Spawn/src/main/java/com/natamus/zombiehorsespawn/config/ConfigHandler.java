/*
 * This is the latest source code of Zombie Horse Spawn.
 * Minecraft version: 1.18.2, mod version: 3.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Zombie Horse Spawn ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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