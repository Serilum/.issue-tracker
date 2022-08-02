/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.treeharvester.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class TreeHarvesterConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> replaceSaplingIfBottomLogIsBroken;
		public final ForgeConfigSpec.ConfigValue<Boolean> mustHoldAxeForTreeHarvest;
		public final ForgeConfigSpec.ConfigValue<Boolean> treeHarvestWithoutSneak;
		public final ForgeConfigSpec.ConfigValue<Boolean> loseDurabilityPerHarvestedLog;
		public final ForgeConfigSpec.ConfigValue<Boolean> increaseExhaustionPerHarvestedLog;
		public final ForgeConfigSpec.ConfigValue<Boolean> instantBreakLeavesAround;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFastLeafDecay;

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
			loseDurabilityPerHarvestedLog = builder
					.comment("If enabled, for every log harvested, the axe held loses durability.")
					.define("loseDurabilityPerHarvestedLog", true);
			increaseExhaustionPerHarvestedLog = builder
					.comment("If enabled, players' exhaustion level increases 0.005 per harvested log (Minecraft's default per broken block).")
					.define("increaseExhaustionPerHarvestedLog", true);
			instantBreakLeavesAround = builder
					.comment("If enabled, players instantly break the leaves as well as all logs of the tree when a bottom log is broken.")
					.define("instantBreakLeavesAround", false);
			enableFastLeafDecay = builder
					.comment("If enabled, the leaves around a broken tree will quickly disappear. Only works with 'instantBreakLeavesAround' disabled.")
					.define("enableFastLeafDecay", true);
			
			builder.pop();
		}
	}
}