/*
 * This is the latest source code of Trample Everything.
 * Minecraft version: 1.19.1, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.trampleeverything.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> _enableTrampledBlockItems;
		public final ForgeConfigSpec.ConfigValue<Boolean> _crouchingPreventsTrampling;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleSnow;
		
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleBambooSaplings;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleCrops;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleDeadBushes;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleDoublePlants;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleFlowers;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleFungi;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleLilyPads;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleMushrooms;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleNetherRoots;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleNetherSprouts;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleNetherWart;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleSaplings;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleSeaGrass;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleSeaPickles;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleStems;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleSugarCane;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleSweetBerryBushes;
		public final ForgeConfigSpec.ConfigValue<Boolean> trampleTallGrass;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
			_enableTrampledBlockItems = builder
					.comment("If enabled, will drop blocks from a trampled block as if a player breaks it by hand.")
					.define("_enableTrampledBlockItems", true);
			_crouchingPreventsTrampling = builder
					.comment("If enabled, crouching/sneaking will prevent any block from being trampled.")
					.define("_crouchingPreventsTrampling", true);
			
			trampleSnow = builder
					.comment("Whether snow should be trampled.")
					.define("trampleSnow", false);
			
			trampleBambooSaplings = builder
					.comment("Whether bamboo saplings should be trampled.")
					.define("trampleBambooSaplings", false);
			trampleCrops = builder
					.comment("Whether growable crops should be trampled.")
					.define("trampleCrops", true);
			trampleDeadBushes = builder
					.comment("Whether dead bushes should be trampled")
					.define("trampleDeadBushes", true);
			trampleDoublePlants = builder
					.comment("Whether double plants should be trampled, for example tall flowers.")
					.define("trampleDoublePlants", true);
			trampleFlowers = builder
					.comment("Whether flowers should be trampled.")
					.define("trampleFlowers", true);
			trampleFungi = builder
					.comment("Whether nether mushrooms should be trampled.")
					.define("trampleFungi", true);
			trampleLilyPads = builder
					.comment("Whether lily pads should be trampled.")
					.define("trampleLilyPads", false);
			trampleMushrooms = builder
					.comment("Whether mushrooms should be trampled.")
					.define("trampleMushrooms", true);
			trampleNetherRoots = builder
					.comment("Whether nether roots should be trampled.")
					.define("trampleNetherRoots", true);
			trampleNetherSprouts = builder
					.comment("Whether nether sprouts should be trampled.")
					.define("trampleNetherSprouts", true);
			trampleNetherWart = builder
					.comment("Whether nether wart should be trampled.")
					.define("trampleNetherWart", true);
			trampleSaplings = builder
					.comment("Whether saplings should be trampled.")
					.define("trampleSaplings", true);
			trampleSeaGrass = builder
					.comment("Whether sea grass should be trampled.")
					.define("trampleSeaGrass", false);
			trampleSeaPickles = builder
					.comment("Whether sea pickles should be trampled.")
					.define("trampleSeaPickles", true);
			trampleStems = builder
					.comment("Whether stems should be trampled, such as pumpkin and melon stems.")
					.define("trampleStems", true);
			trampleSugarCane = builder
					.comment("Whether sugar cane should be trampled.")
					.define("trampleSugarCane", false);
			trampleSweetBerryBushes = builder
					.comment("Whether sweet berry bushes should be trampled.")
					.define("trampleSweetBerryBushes", false);
			trampleTallGrass = builder
					.comment("Whether tall grass should be trampled.")
					.define("trampleTallGrass", true);
			
			builder.pop();
		}
	}
}