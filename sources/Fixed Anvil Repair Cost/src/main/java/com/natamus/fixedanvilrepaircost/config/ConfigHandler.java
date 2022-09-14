/*
 * This is the latest source code of Fixed Anvil Repair Cost.
 * Minecraft version: 1.19.2, mod version: 1.9.
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

package com.natamus.fixedanvilrepaircost.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Integer> repairCostLevelAmount;
		public final ForgeConfigSpec.ConfigValue<Integer> repairCostMaterialAmount;
		public final ForgeConfigSpec.ConfigValue<Double> percentRepairedPerAction;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			repairCostLevelAmount = builder
					.comment("The amount of level it costs to repair an item on an anvil. A value of 0 keeps vanilla behaviour.")
					.defineInRange("repairCostLevelAmount", 3, 0, 100);
			repairCostMaterialAmount = builder
					.comment("The amount of material it costs to repair an item on an anvil. A value of 0 keeps vanilla behaviour.")
					.defineInRange("repairCostMaterialAmount", 1, 0, 64);
			percentRepairedPerAction = builder
					.comment("How much the item is repaired per action. By default 33.33%, so 3 of 'repairCostMaterialAmount' fully repairs any item.")
					.defineInRange("percentRepairedPerAction", 0.3333, 0, 1.0);
			
			builder.pop();
		}
	}
}