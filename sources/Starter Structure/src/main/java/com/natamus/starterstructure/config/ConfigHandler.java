/*
 * This is the latest source code of Starter Structure.
 * Minecraft version: 1.19.3, mod version: 1.2.
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

package com.natamus.starterstructure.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> shouldGenerateStructure;
		public final ForgeConfigSpec.ConfigValue<Boolean> forceExactSpawnMiddleStructure;
		public final ForgeConfigSpec.ConfigValue<Integer> generatedStructureYOffset;
		public final ForgeConfigSpec.ConfigValue<Boolean> ignoreTreesDuringStructurePlacement;
		public final ForgeConfigSpec.ConfigValue<Boolean> generationIgnoreJigsawAndStructureBlocks;

		public final ForgeConfigSpec.ConfigValue<Boolean> protectStructureBlocks;
		public final ForgeConfigSpec.ConfigValue<Boolean> protectSpawnedEntities;
		public final ForgeConfigSpec.ConfigValue<Boolean> playersInCreativeModeIgnoreProtection;
		public final ForgeConfigSpec.ConfigValue<Boolean> playersInCreativeModeIgnoreEntityProtection;
		public final ForgeConfigSpec.ConfigValue<Boolean> preventSpawnedEntityMovement;

		public final ForgeConfigSpec.ConfigValue<Boolean> shouldSetSpawnPoint;
		public final ForgeConfigSpec.ConfigValue<Integer> spawnXCoordinate;
		public final ForgeConfigSpec.ConfigValue<Integer> spawnYCoordinate;
		public final ForgeConfigSpec.ConfigValue<Integer> spawnZCoordinate;

		public final ForgeConfigSpec.ConfigValue<Boolean> spawnNonSignEntitiesFromSupportedSchematics;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			shouldGenerateStructure = builder
					.comment("Whether a schematic that's located in './config/starterstructure/schematics/...' should be generated.")
					.define("shouldGenerateStructure", true);
			forceExactSpawnMiddleStructure = builder
					.comment("Usually player spawn points are in a randomized area. With this enabled, players will always spawn in the middle of the structure (at the nearest air pocket).")
					.define("forceExactSpawnMiddleStructure", true);
			generatedStructureYOffset = builder
					.comment("The y offset for the generated structure. Can for example be set to -1 if you notice a building always spawns one block too high.")
					.defineInRange("generatedStructureYOffset", 0, -100, 100);
			ignoreTreesDuringStructurePlacement = builder
					.comment("Prevents structures from being placed on top of trees. Any leaf and log blocks will be ignored during placement.")
					.define("ignoreTreesDuringStructurePlacement", true);
			generationIgnoreJigsawAndStructureBlocks = builder
					.comment("Some schematic files might contain jigsaw or structure blocks. These are by default ignored during structure generation.")
					.define("generationIgnoreJigsawAndStructureBlocks", true);

			protectStructureBlocks = builder
					.comment("Whether the blocks from the generated structure should be protected from breaking/griefing.")
					.define("protectStructureBlocks", true);
			protectSpawnedEntities = builder
					.comment("Whether entities spawned inside the generated structure should be protected from damage.")
					.define("protectSpawnedEntities", true);
			playersInCreativeModeIgnoreProtection = builder
					.comment("If enabled, players that are in creative mode will be able to break and place the structure blocks.")
					.define("playersInCreativeModeIgnoreProtection", true);
			playersInCreativeModeIgnoreEntityProtection = builder
					.comment("If enabled, players that are in creative mode will be able to damage protected entities which spawned in structures.")
					.define("playersInCreativeModeIgnoreEntityProtection", false);
			preventSpawnedEntityMovement = builder
					.comment("If spawned entities inside the generated structure should not be allowed to move away from the block they spawned on. Disabled by default.")
					.define("preventSpawnedEntityMovement", false);

			shouldSetSpawnPoint = builder
					.comment("Whether the initial world spawn point should be set to specific coordinates.")
					.define("shouldSetSpawnPoint", false);
			spawnXCoordinate = builder
					.comment("The new X coordinate of the spawn when shouldSetSpawnPoint is enabled.")
					.defineInRange("spawnXCoordinate", 0, -10000000, 10000000);
			spawnYCoordinate = builder
					.comment("The new Y coordinate of the spawn when shouldSetSpawnPoint is enabled. A value of -1 means on the surface.")
					.defineInRange("spawnYCoordinate", -1, -1, 10000);
			spawnZCoordinate = builder
					.comment("The new Z coordinate of the spawn when shouldSetSpawnPoint is enabled.")
					.defineInRange("spawnZCoordinate", 0, -10000000, 10000000);

			spawnNonSignEntitiesFromSupportedSchematics = builder
					.comment("If entities from (structure block) schematic files should be spawned when found. These are entities not created with signs.")
					.define("spawnNonSignEntitiesFromSupportedSchematics", true);
			
			builder.pop();
		}
	}
}