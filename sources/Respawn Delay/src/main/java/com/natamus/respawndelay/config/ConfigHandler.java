/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.18.1, mod version: 2.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Respawn Delay ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.respawndelay.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {		
		public final ForgeConfigSpec.ConfigValue<Boolean> ignoreAdministratorPlayers;
		public final ForgeConfigSpec.ConfigValue<Boolean> ignoreCreativePlayers;
		public final ForgeConfigSpec.ConfigValue<Boolean> keepItemsOnDeath;
		public final ForgeConfigSpec.ConfigValue<Boolean> dropItemsOnDeath;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> respawnAtWorldSpawn;
		public final ForgeConfigSpec.ConfigValue<Boolean> respawnWhenPlayerLogsOut;
		public final ForgeConfigSpec.ConfigValue<Boolean> respawnWhenPlayerLogsIn;
		
		public final ForgeConfigSpec.ConfigValue<Integer> respawnDelayInSeconds;
		
		public final ForgeConfigSpec.ConfigValue<String> onDeathMessage;
		public final ForgeConfigSpec.ConfigValue<String> onRespawnMessage;
		public final ForgeConfigSpec.ConfigValue<String> waitingForRespawnMessage;
		
		public final ForgeConfigSpec.ConfigValue<Integer> lowestPossibleYCoordinate;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			ignoreAdministratorPlayers = builder
					.comment("If enabled, player operators/administrators will not be put in spectator mode on death.")
					.define("ignoreAdministratorPlayers", false);
			ignoreCreativePlayers = builder
					.comment("If enabled, player in creative mode will not be put in spectator mode on death.")
					.define("ignoreCreativePlayers", true);
			keepItemsOnDeath = builder
					.comment("If enabled, players will keep their items on death, and no drop event will be ran. This will also ignore the 'dropItemsOnDeath' config.")
					.define("keepItemsOnDeath", false);
			dropItemsOnDeath = builder
					.comment("If enabled, players will drop their items on death as normal vanilla behaviour.")
					.define("dropItemsOnDeath", true);

			respawnAtWorldSpawn = builder
					.comment("If enabled, players will be respawned at the world spawn point. If disabled, they'll respawn wherever they're spectating.")
					.define("respawnAtWorldSpawn", true);
			respawnWhenPlayerLogsOut = builder
					.comment("If enabled, players will respawn when they logout while waiting for a respawn. Prevents players from getting stuck in spectator mode.")
					.define("respawnWhenPlayerLogsOut", true);
			respawnWhenPlayerLogsIn = builder
					.comment("If enabled, players will respawn when they log in and are still in spectator mode. Prevents players from getting stuck in spectator mode.")
					.define("respawnWhenPlayerLogsIn", true);
			
			respawnDelayInSeconds = builder
					.comment("The delay in seconds after which a spectating player will respawn. A value of -1 makes players never respawn automatically. The '/respawnall' command can still be used.")
					.defineInRange("respawnDelayInSeconds", 10, -1, 3600);
			
			onDeathMessage = builder
					.comment("The message which is sent to the player on death. Leave empty to disable.")
					.define("onDeathMessage", "You died! Your gamemode has been set to spectator.");
			onRespawnMessage = builder
					.comment("The message which is sent to players when they respawn. Leave empty to disable.")
					.define("onRespawnMessage", "You respawned.");
			waitingForRespawnMessage = builder
					.comment("The message which is sent to players when they are waiting to be respawned. The text '<seconds_left>' will be replaced with the actual seconds left. Leave empty to disable.")
					.define("waitingForRespawnMessage", "You will respawn in <seconds_left> seconds.");
			
			lowestPossibleYCoordinate = builder
					.comment("When a player falls into the void, this determines the y position that's set after when a player enters spectator mode.")
					.defineInRange("lowestPossibleYCoordinate", -10, -50, 256);

			
			builder.pop();
		}
	}
}