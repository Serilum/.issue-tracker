/*
 * This is the latest source code of Mineral Chance.
 * Minecraft version: 1.17.1, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Mineral Chance ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.mineralchance.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Double> extraMineralChanceOnStoneBreak;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableOverworldMinerals;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNetherMinerals;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnMineralFind;
		public final ForgeConfigSpec.ConfigValue<String> foundMineralMessage;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> ignoreFakePlayers;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			extraMineralChanceOnStoneBreak = builder
					.comment("The chance a mineral is dropped when a stone block is broken. By default 1/50. Overworld stone blocks are stone, andesite, granite and diorite. Nether stone blocks are netherrack.")
					.defineInRange("extraMineralChanceOnStoneBreak", 0.02, 0, 1.0);
			enableOverworldMinerals = builder
					.comment("If enabled, mining overworld stone blocks in the overworld has a chance to drop an overworld mineral. These consist of diamonds, gold nuggets, iron nuggets, lapis lazuli, redstone and emeralds.")
					.define("enableOverworldMinerals", true);
			enableNetherMinerals = builder
					.comment("If enabled, mining nether stone blocks in the nether has a chance to drop a nether mineral. These consist of quartz and gold nuggets.")
					.define("enableNetherMinerals", true);
			
			sendMessageOnMineralFind = builder
					.comment("If enabled, sends a message when a mineral is found to the player who broke the stone block.")
					.define("sendMessageOnMineralFind", true);
			foundMineralMessage = builder
					.comment("The message sent to the player who found a hidden mineral when 'sendMessageOnMineralFind' is enabled.")
					.define("foundMineralMessage", "You've found a mineral hidden in the block!");
			
			ignoreFakePlayers = builder
					.comment("If enabled, minerals won't be dropped if the player is a fake. For example when a mod breaks a block as a simulated player.")
					.define("ignoreFakePlayers", true);
			
			builder.pop();
		}
	}
}