/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.18.0, mod version: 4.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Tree Harvester ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.treeharvester.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> replaceSaplingIfBottomLogIsBroken;
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHoldAxeForTreeHarvest;
		public final ForgeConfigSpec.ConfigValue<Boolean> treeHarvestWithoutSneak;
		public final ForgeConfigSpec.ConfigValue<Boolean> instantBreakLeavesAround;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFastLeafDecay;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNetherTrees;
		public final ForgeConfigSpec.ConfigValue<Boolean> automaticallyFindBottomBlock;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> loseDurabilityPerHarvestedLog;
		public final ForgeConfigSpec.ConfigValue<Double> loseDurabilityModifier;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> increaseExhaustionPerHarvestedLog;
		public final ForgeConfigSpec.ConfigValue<Double> increaseExhaustionModifier;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			replaceSaplingIfBottomLogIsBroken = builder
					.comment("If enabled, automatically replaces the sapling from the drops when the bottom log is broken and the player is not holding the sneak button.")
					.define("replaceSaplingIfBottomLogIsBroken", true);
			mustHoldAxeForTreeHarvest = builder
					.comment("If enabled, tree harvesting only works when a player is holding an axe in the main hand.")
					.define("mustHoldAxeForTreeHarvest", true);
			treeHarvestWithoutSneak = builder
					.comment("If enabled, tree harvesting works when not holding the sneak button. If disabled it's reversed, and only works when sneaking.")
					.define("treeHarvestWithoutSneak", false);
			instantBreakLeavesAround = builder
					.comment("If enabled, players instantly break the leaves as well as all logs of the tree when a bottom log is broken.")
					.define("instantBreakLeavesAround", false);
			enableFastLeafDecay = builder
					.comment("If enabled, the leaves around a broken tree will quickly disappear. Only works with 'instantBreakLeavesAround' disabled.")
					.define("enableFastLeafDecay", true);
			enableNetherTrees = builder
					.comment("If enabled, the warped stem/crimson trees in the nether will also be chopped down quickly.")
					.define("enableNetherTrees", true);
			automaticallyFindBottomBlock = builder
					.comment("Whether the mod should attempt to find the actual bottom log of the tree and start there. This means you can break a tree in the middle and it will still completely be felled.")
					.define("automaticallyFindBottomBlock", true);
			
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
			
			builder.pop();
		}
	}
}