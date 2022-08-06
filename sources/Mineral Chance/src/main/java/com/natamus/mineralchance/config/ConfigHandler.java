/*
 * This is the latest source code of Mineral Chance.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.mineralchance.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Double> extraMineralChanceOnOverworldStoneBreak;
		public final ForgeConfigSpec.ConfigValue<Double> extraMineralChanceOnNetherStoneBreak;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableOverworldMinerals;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNetherMinerals;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> sendMessageOnMineralFind;
		public final ForgeConfigSpec.ConfigValue<String> foundMineralMessage;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> ignoreFakePlayers;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			extraMineralChanceOnOverworldStoneBreak = builder
					.comment("The chance a mineral is dropped when an overworld stone block is broken. By default 1/50.")
					.defineInRange("extraMineralChanceOnOverworldStoneBreak", 0.02, 0, 1.0);
			extraMineralChanceOnNetherStoneBreak = builder
					.comment("The chance a mineral is dropped when a nether stone block is broken. By default 1/100.")
					.defineInRange("extraMineralChanceOnNetherStoneBreak", 0.01, 0, 1.0);
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