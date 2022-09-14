/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.19.2, mod version: 5.3.
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

package com.natamus.treeharvester.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHoldAxeForTreeHarvest;
		public final ForgeConfigSpec.ConfigValue<Boolean> treeHarvestWithoutSneak;
		public final ForgeConfigSpec.ConfigValue<Boolean> instantBreakLeavesAround;
		public final ForgeConfigSpec.ConfigValue<Boolean> automaticallyFindBottomBlock;

		public final ForgeConfigSpec.ConfigValue<Boolean> enableFastLeafDecay;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNetherTrees;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableHugeMushrooms;

		public final ForgeConfigSpec.ConfigValue<Boolean> replaceSaplingOnTreeHarvest;
		public final ForgeConfigSpec.ConfigValue<Boolean> replaceMushroomOnMushroomHarvest;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> loseDurabilityPerHarvestedLog;
		public final ForgeConfigSpec.ConfigValue<Double> loseDurabilityModifier;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> increaseExhaustionPerHarvestedLog;
		public final ForgeConfigSpec.ConfigValue<Double> increaseExhaustionModifier;

		public final ForgeConfigSpec.ConfigValue<Boolean> increaseHarvestingTimePerLog;
		public final ForgeConfigSpec.ConfigValue<Double> increasedHarvestingTimePerLogModifier;

		public final ForgeConfigSpec.ConfigValue<Integer> amountOfLeavesBrokenPerTick;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			mustHoldAxeForTreeHarvest = builder
					.comment("If enabled, tree harvesting only works when a player is holding an axe in the main hand.")
					.define("mustHoldAxeForTreeHarvest", true);
			treeHarvestWithoutSneak = builder
					.comment("If enabled, tree harvesting works when not holding the sneak button. If disabled it's reversed, and only works when sneaking.")
					.define("treeHarvestWithoutSneak", false);
			instantBreakLeavesAround = builder
					.comment("If enabled, players instantly break the leaves as well as all logs of the tree when a bottom log is broken.")
					.define("instantBreakLeavesAround", false);
			automaticallyFindBottomBlock = builder
					.comment("Whether the mod should attempt to find the actual bottom log of the tree and start there. This means you can break a tree in the middle and it will still completely be felled.")
					.define("automaticallyFindBottomBlock", true);

			enableFastLeafDecay = builder
					.comment("If enabled, the leaves around a broken tree will quickly disappear. Only works with 'instantBreakLeavesAround' disabled.")
					.define("enableFastLeafDecay", true);
			enableNetherTrees = builder
					.comment("If enabled, the warped stem/crimson trees in the nether will also be chopped down quickly.")
					.define("enableNetherTrees", true);
			enableHugeMushrooms = builder
					.comment("If enabled, giant/huge mushrooms will also be chopped down quickly.")
					.define("enableHugeMushrooms", true);

			replaceSaplingOnTreeHarvest = builder
					.comment("If enabled, automatically replaces the sapling from the drops when a tree is harvested.")
					.define("replaceSaplingOnTreeHarvest", true);
			replaceMushroomOnMushroomHarvest = builder
					.comment("If enabled, automatically replaces the sapling from the drops when a huge mushroom is harvested and 'enableHugeMushrooms' is enabled.")
					.define("replaceMushroomOnMushroomHarvest", true);
			
			loseDurabilityPerHarvestedLog = builder
					.comment("If enabled, for every log harvested, the axe held loses durability.")
					.define("loseDurabilityPerHarvestedLog", true);
			loseDurabilityModifier = builder
					.comment("Here you can set how much durability chopping down a tree should take from the axe. For example if set to 0.1, this means that every 10 logs take 1 durability.")
					.defineInRange("loseDurabilityModifier", 1.0, 0.001, 1.0);
			
			increaseExhaustionPerHarvestedLog = builder
					.comment("If enabled, players' exhaustion level increases 0.005 per harvested log (Minecraft's default per broken block) * increaseExhaustionModifier.")
					.define("increaseExhaustionPerHarvestedLog", true);
			increaseExhaustionModifier = builder
					.comment("This determines how much exhaustion should be added to the player per harvested log. By default 0.005 * 1.0.")
					.defineInRange("increaseExhaustionModifier", 1.0, 0.001, 1.0);

			increaseHarvestingTimePerLog = builder
					.comment("If enabled, harvesting time will increase per existing log in the tree. The amount is determined by 'increasedHarvestingTimePerLogModifier'.")
					.define("increaseHarvestingTimePerLog", true);
			increasedHarvestingTimePerLogModifier = builder
					.comment("How much longer it takes to harvest a tree with 'increaseHarvestingTimePerLog' enabled. The actual speed is: newSpeed = originalSpeed / (1 + (logCount * increasedHarvestingTimePerLogModifier)).")
					.defineInRange("increasedHarvestingTimePerLogModifier", 0.1, 0.01, 10.0);

			amountOfLeavesBrokenPerTick = builder
					.comment("How many leaves should be broken per tick after a tree has been harvested. Increasing this will speed up the fast leaf decay, but costs more processing power per tick.")
					.defineInRange("amountOfLeavesBrokenPerTick", 3, 1, 16);
			
			builder.pop();
		}
	}
}