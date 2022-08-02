/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.campfirespawnandtweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CampfireSpawnandTweaksConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> campfiresStartUnlit;		
		public final ForgeConfigSpec.ConfigValue<Boolean> sneakRightClickCampfireToUnset;
		public final ForgeConfigSpec.ConfigValue<Boolean> bedsOverrideCampfireSpawnOnSneakRightClick;
		public final ForgeConfigSpec.ConfigValue<Boolean> createAirPocketIfBlocksAboveCampfire;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnNewCampfireSpawnSet;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnCampfireSpawnUnset;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnCampfireSpawnMissing;
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnCampfireSpawnOverride;

		public final ForgeConfigSpec.ConfigValue<Integer> fireResitanceDurationOnRespawnInMs;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			campfiresStartUnlit = builder
					.comment("When enabled, a newly placed campfire will be unlit.")
					.define("campfiresStartUnlit", true);			
			sneakRightClickCampfireToUnset = builder
					.comment("Crouching/Sneaking and right-clicking on a campfire unsets the campfire spawn point.")
					.define("sneakRightClickCampfireToUnset", true);
			bedsOverrideCampfireSpawnOnSneakRightClick = builder
					.comment("When enabled, sneak/crouch + right-clicking a bed will override the campfire spawn point.")
					.define("bedsOverrideCampfireSpawnOnSneakRightClick", true);
			createAirPocketIfBlocksAboveCampfire = builder
					.comment("When enabled, the mod breaks the blocks above a campfire on respawn if it would somehow be blocked.")
					.define("createAirPocketIfBlocksAboveCampfire", true);
			
			sendMessageOnNewCampfireSpawnSet = builder
					.comment("When enabled, a message will be sent to the player whenever a new campfire spawn point is set.")
					.define("sendMessageOnNewCampfireSpawnSet", true);
			sendMessageOnCampfireSpawnUnset = builder
					.comment("When enabled, a message will be sent to the player whenever a campfire spawn point is unset.")
					.define("sendMessageOnCampfireSpawnUnset", true);
			sendMessageOnCampfireSpawnMissing = builder
					.comment("When enabled, a message will be sent to the player whenever a campfire spawn point is missing on respawn.")
					.define("sendMessageOnCampfireSpawnMissing", true);
			sendMessageOnCampfireSpawnOverride = builder
					.comment("When enabled, a message will be sent to the player whenever a campfire spawn point is overridden by the PlayerSetSpawnEvent.")
					.define("sendMessageOnCampfireSpawnOverride", true);
			
			fireResitanceDurationOnRespawnInMs = builder
					.comment("The duration of fire resistance when a player respawns at a campfire. A value of 0 disables this feature, and places the player next to the campfire instead.")
					.defineInRange("fireResitanceDurationOnRespawnInMs", 10000, 0, 3600000);
			
			builder.pop();
		}
	}
}