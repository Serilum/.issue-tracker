/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.56.
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

package com.natamus.collective.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final Collective COLLECTIVE = new Collective(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class Collective {		
		public final ForgeConfigSpec.ConfigValue<Boolean> transferItemsBetweenReplacedEntities;
		public final ForgeConfigSpec.ConfigValue<Integer> loopsAmountUsedToGetAllEntityDrops;
		public final ForgeConfigSpec.ConfigValue<Integer> findABlockcheckAroundEntitiesDelayMs;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> enableAntiRepostingCheck;
		public final ForgeConfigSpec.ConfigValue<Boolean> enablePatronPets;

		public Collective(ForgeConfigSpec.Builder builder) {
			builder.push("Collective");
			transferItemsBetweenReplacedEntities = builder
					.comment("When enabled, transfer the held items and armour from replaced entities by any of the Entity Spawn mods which depend on Collective.")
					.define("transferItemsBetweenReplacedEntities", true);
			loopsAmountUsedToGetAllEntityDrops = builder
					.comment("The amount of times Collective loops through possible mob drops to get them all procedurally. Drops are only generated when a dependent mod uses them. Lowering this can increase world load time but decrease accuracy.")
					.defineInRange("loopsAmountUsedToGetAllEntityDrops", 100, 1, 500);
			findABlockcheckAroundEntitiesDelayMs = builder
					.comment("The delay of the is-there-a-block-around-check around entities in ms. Used in mods which depends on a specific blockstate in the world. Increasing this number can increase TPS if needed.")
					.defineInRange("findABlockcheckAroundEntitiesDelayMs", 30000, 0, 3600000);
			
			enableAntiRepostingCheck = builder
					.comment("Please check out https://stopmodreposts.org/ for more information on why this feature exists.")
					.define("enableAntiRepostingCheck", true);
			enablePatronPets = builder
					.comment("Enables pets for Patrons. Will be added in a future release.")
					.define("enablePatronPets", true);
			
			builder.pop();
		}
	}
}