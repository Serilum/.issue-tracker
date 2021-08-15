/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigHandler {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final General GENERAL = new General(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();

	public static class General {
		public final ForgeConfigSpec.ConfigValue<Boolean> _enableALL;








		public final ForgeConfigSpec.ConfigValue<Boolean> enableAdvancementScreenshot;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableAllLootDrops;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableAlwaysaWitherSkull;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableAreas;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableAutomaticDoors;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableBeautifiedChatServer;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableBetterBeaconPlacement;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableBetterConduitPlacement;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableBetterSpawnerControl;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableBiggerSpongeAbsorptionRadius;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableBottleYourXp;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableBottledAir;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableBouncierBeds;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableBreedableKillerRabbit;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableCampfireSpawnandTweaks;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableConduitsPreventDrowned;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableConfigurableDespawnTimer;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableCreativeBlockReplacer;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableCryingGhasts;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableCyclePaintings;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDeathBackup;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDespawningEggsHatch;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDismountEntity;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDoubleDoors;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableDragonDropsElytra;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableEasyElytraTakeoff;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableEdibles;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableEnchantingCommands;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableEndPortalRecipe;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableErodingStoneEntities;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableExtendedBoneMeal;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableExtendedCreativeInventory;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableExtractPoison;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFallThroughSlime;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFireSpreadTweaks;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFirstJoinMessage;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFishOnTheLine;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableFixedAnvilRepairCost;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableGiantSpawn;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableGrassSeeds;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableGrindstoneSharperTools;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableGUIClock;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableGUICompass;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableGUIFollowers;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableHandOverYourItems;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableHealingCampfire;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableHideHands;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableHuskSpawn;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableIcePreventsCropGrowth;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableInfiniteTrading;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableInventoryTotem;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableJustMobHeads;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableJustPlayerHeads;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableKelpFertilizer;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMilkAllTheMobs;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMineralChance;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMooshroomSpawn;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMooshroomTweaks;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMoreZombieVillagers;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMoveBoats;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableMoveMinecarts;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNameTagTweaks;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNaturallyChargedCreepers;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNetherPortalSpread;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNoHostilesAroundCampfire;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableNutritiousMilk;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableOmegaMute;
		public final ForgeConfigSpec.ConfigValue<Boolean> enablePaperBooks;
		public final ForgeConfigSpec.ConfigValue<Boolean> enablePassiveEndermen;
		public final ForgeConfigSpec.ConfigValue<Boolean> enablePassiveShield;
		public final ForgeConfigSpec.ConfigValue<Boolean> enablePetNames;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableQuickPaths;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableRainBeGoneRitual;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableRandomBoneMealFlowers;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableRandomSheepColours;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableRandomShulkerColours;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableRandomVillageNames;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableRealisticBees;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableRecast;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableRecipeCommands;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableReplantingCrops;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableRespawningShulkers;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableScaffoldingDropsNearby;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableShulkerDropsTwo;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableSkeletonHorseSpawn;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableSleepSooner;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableSmallerNetherPortals;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableSnowballsFreezeMobs;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableSofterHayBales;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableSpidersProduceWebs;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableStackRefill;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableStarterKit;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableStraySpawn;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableSuperflatWorldNoSlimes;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableTNTBreaksBedrock;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableTranscendingTrident;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableTreeHarvester;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableUnderwaterEnchanting;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableVillageBellRecipe;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableVillageSpawnPoint;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableVillagerDeathMessages;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableVillagerNames;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableWeakerSpiderwebs;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableWoolTweaks;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableZombieHorseSpawn;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableZombieProofDoors;
		public final ForgeConfigSpec.ConfigValue<Boolean> enableZombieVillagersFromSpawner;

		public General(ForgeConfigSpec.Builder builder) {
			builder.push("General");
		
			_enableALL = builder
					.comment("If ALL mods should be enabled. Ignores the other settings when enabled.")
					.define("_enableALL", true);








			enableAdvancementScreenshot = builder
					.comment("If Advancement Screenshot should be enabled. A client-side mod which takes a screenshot every time a player achieves an advancement.")
					.define("enableAdvancementScreenshot", false);
			enableAllLootDrops = builder
					.comment("If All Loot Drops should be enabled. A minimalistic mod that has entities always drop all their possible loot in a configurable amount.")
					.define("enableAllLootDrops", false);
			enableAlwaysaWitherSkull = builder
					.comment("If Always a Wither Skull should be enabled. A drag-and-drop mod that makes wither skeletons always drop their skull on death.")
					.define("enableAlwaysaWitherSkull", false);
			enableAreas = builder
					.comment("If Areas should be enabled. Create custom named areas/towns with a radius using signs, which sends join and leave messages.")
					.define("enableAreas", false);
			enableAutomaticDoors = builder
					.comment("If Automatic Doors should be enabled. Automatically opens doors in front of the player and closes them with a configurable delay.")
					.define("enableAutomaticDoors", false);
			enableBeautifiedChatServer = builder
					.comment("If Beautified Chat Server should be enabled. A highly configurable mod to change the style and colour of incoming chat messages server-wide.")
					.define("enableBeautifiedChatServer", false);
			enableBetterBeaconPlacement = builder
					.comment("If Better Beacon Placement should be enabled. Easily place beacon bases by right-clicking the beacon with a mineral block plus other tweaks.")
					.define("enableBetterBeaconPlacement", false);
			enableBetterConduitPlacement = builder
					.comment("If Better Conduit Placement should be enabled. Place conduits in front of you, place blocks by clicking the conduit and other tweaks.")
					.define("enableBetterConduitPlacement", false);
			enableBetterSpawnerControl = builder
					.comment("If Better Spawner Control should be enabled. A drag-and-drop mod which disables mob spawners whenever they are surrounded by 5 torches.")
					.define("enableBetterSpawnerControl", false);
			enableBiggerSpongeAbsorptionRadius = builder
					.comment("If Bigger Sponge Absorption Radius should be enabled. Each extra sponge block placed next to another increases the water absorption radius.")
					.define("enableBiggerSpongeAbsorptionRadius", false);
			enableBottleYourXp = builder
					.comment("If Bottle Your Xp should be enabled. A minimalistic configurable mod that allows the creation of experience bottles from your own gathered xp.")
					.define("enableBottleYourXp", false);
			enableBottledAir = builder
					.comment("If Bottled Air should be enabled. Allows players to breathe in empty glass bottles underwater to get more air, configurable.")
					.define("enableBottledAir", false);
			enableBouncierBeds = builder
					.comment("If Bouncier Beds should be enabled. Makes players bounce configurably higher on beds and prevents fall damage on them.")
					.define("enableBouncierBeds", false);
			enableBreedableKillerRabbit = builder
					.comment("If Breedable Killer Rabbit should be enabled. A minimalistic configurable mod which makes the killer rabbit tamable and available by breeding.")
					.define("enableBreedableKillerRabbit", false);
			enableCampfireSpawnandTweaks = builder
					.comment("If Campfire Spawn and Tweaks should be enabled. Allows player to set spawn points inside a campfire, gives fire resistance on respawn and does some other tweaks.")
					.define("enableCampfireSpawnandTweaks", false);
			enableConduitsPreventDrowned = builder
					.comment("If Conduits Prevent Drowned should be enabled. Disables drowned zombie spawns around the player when the conduit effect is active.")
					.define("enableConduitsPreventDrowned", false);
			enableConfigurableDespawnTimer = builder
					.comment("If Configurable Despawn Timer should be enabled. Allows items to remain on the ground longer or shorter than the default lifespan.")
					.define("enableConfigurableDespawnTimer", false);
			enableCreativeBlockReplacer = builder
					.comment("If Creative Block Replacer should be enabled. Allows easy switching in between placing and replacing blocks in creative mode.")
					.define("enableCreativeBlockReplacer", false);
			enableCryingGhasts = builder
					.comment("If Crying Ghasts should be enabled. Ghasts drop their tears periodically when a player is close, giving the ability to farm them sustainably.")
					.define("enableCryingGhasts", false);
			enableCyclePaintings = builder
					.comment("If Cycle Paintings should be enabled. Easily cycle through placed paintings by right-clicking them with another painting.")
					.define("enableCyclePaintings", false);
			enableDeathBackup = builder
					.comment("If Death Backup should be enabled. Creates a backup of an inventory right before a player death, which can be loaded via a command.")
					.define("enableDeathBackup", false);
			enableDespawningEggsHatch = builder
					.comment("If Despawning Eggs Hatch should be enabled. Allows chicken eggs on a hay block to hatch into a chick baby just before they despawn.")
					.define("enableDespawningEggsHatch", false);
			enableDismountEntity = builder
					.comment("If Dismount Entity should be enabled. Allows players to dismount/remove/exit entities/mobs from mounted entities.")
					.define("enableDismountEntity", false);
			enableDoubleDoors = builder
					.comment("If Double Doors should be enabled. Adds the ability for identical double doors, trapdoors and fence gates to be opened simultaneously via click or pressure plates and buttons.")
					.define("enableDoubleDoors", false);
			enableDragonDropsElytra = builder
					.comment("If Dragon Drops Elytra should be enabled. A minimalistic mod that makes the Ender Dragon drop an elytra on death.")
					.define("enableDragonDropsElytra", false);
			enableEasyElytraTakeoff = builder
					.comment("If Easy Elytra Takeoff should be enabled. Allows easy taking off by elytra with a firework from the ground without that awkward jump.")
					.define("enableEasyElytraTakeoff", false);
			enableEdibles = builder
					.comment("If Edibles should be enabled. Makes lots of ingredients edible, after which the player receives a short status effect.")
					.define("enableEdibles", false);
			enableEnchantingCommands = builder
					.comment("If Enchanting Commands should be enabled. A minimalistic mod which allows for easy creation of enchanted items via commands with levels above the default limit. Usage: /ec")
					.define("enableEnchantingCommands", false);
			enableEndPortalRecipe = builder
					.comment("If End Portal Recipe should be enabled. Makes the end portal craftable and available in survival plus other tweaks.")
					.define("enableEndPortalRecipe", false);
			enableErodingStoneEntities = builder
					.comment("If Eroding Stone Entities should be enabled. When stone-type entities are left in flowing water for some time they turn into sand.")
					.define("enableErodingStoneEntities", false);
			enableExtendedBoneMeal = builder
					.comment("If Extended Bone Meal should be enabled. Extends bone meal behaviour. Instant grow with sneak, grow netherwart and other tweaks.")
					.define("enableExtendedBoneMeal", false);
			enableExtendedCreativeInventory = builder
					.comment("If Extended Creative Inventory should be enabled. Adds all items which by default aren't in the creative inventory to a custom Extended tab.")
					.define("enableExtendedCreativeInventory", false);
			enableExtractPoison = builder
					.comment("If Extract Poison should be enabled. Allows the extraction of poison from cave spiders, pufferfish and bees with an empty bottle.")
					.define("enableExtractPoison", false);
			enableFallThroughSlime = builder
					.comment("If Fall Through Slime should be enabled. Slime blocks no longer support your weight when standing still, and you'll slowly fall through.")
					.define("enableFallThroughSlime", false);
			enableFireSpreadTweaks = builder
					.comment("If Fire Spread Tweaks should be enabled. Disables the default firespread, but does make fire go out on its own after a configurable delay.")
					.define("enableFireSpreadTweaks", false);
			enableFirstJoinMessage = builder
					.comment("If First Join Message should be enabled. Sends players who join a world for the first time a configurable message.")
					.define("enableFirstJoinMessage", false);
			enableFishOnTheLine = builder
					.comment("If Fish On The Line should be enabled. Rings the bell held in your offhand when you are fishing and something's on the line.")
					.define("enableFishOnTheLine", false);
			enableFixedAnvilRepairCost = builder
					.comment("If Fixed Anvil Repair Cost should be enabled. Allows setting the level and material cost of repairing an item on an anvil to always be the same configurable amount and also allows setting how much percent is repaired.")
					.define("enableFixedAnvilRepairCost", false);
			enableGiantSpawn = builder
					.comment("If Giant Spawn should be enabled. Allows the giant to spawn naturally and other tweaks.")
					.define("enableGiantSpawn", false);
			enableGrassSeeds = builder
					.comment("If Grass Seeds should be enabled. A minimalistic mod which allows players to use normal seeds to transform dirt to grass.")
					.define("enableGrassSeeds", false);
			enableGrindstoneSharperTools = builder
					.comment("If Grindstone Sharper Tools should be enabled. The grindstone can temporarily increase the damage of tools used on it.")
					.define("enableGrindstoneSharperTools", false);
			enableGUIClock = builder
					.comment("If GUI Clock should be enabled. Shows the time and days played in the GUI on the screen with a clock in the inventory.")
					.define("enableGUIClock", false);
			enableGUICompass = builder
					.comment("If GUI Compass should be enabled. Shows the direction and coordinates in the GUI with a compass in the inventory.")
					.define("enableGUICompass", false);
			enableGUIFollowers = builder
					.comment("If GUI Followers should be enabled. [Client] Shows a list in the GUI/HUD with all tamed non-sitting followers around the player.")
					.define("enableGUIFollowers", false);
			enableHandOverYourItems = builder
					.comment("If Hand Over Your Items should be enabled. Adds to ability to give other players items without dropping them on the floor.")
					.define("enableHandOverYourItems", false);
			enableHealingCampfire = builder
					.comment("If Healing Campfire should be enabled. A minimalistic mod which creates an area around the campfire where players and passive mobs receive the regeneration effect in a configurable radius.")
					.define("enableHealingCampfire", false);
			enableHideHands = builder
					.comment("If Hide Hands should be enabled. Hide your offhand and/or mainhand, always or while holding specific items.")
					.define("enableHideHands", false);
			enableHuskSpawn = builder
					.comment("If Husk Spawn should be enabled. Adds a chance for husks to spawn naturally everywhere instead of a zombie.")
					.define("enableHuskSpawn", false);
			enableIcePreventsCropGrowth = builder
					.comment("If Ice Prevents Crop Growth should be enabled. Crops growing on tilled soil with an ice block underneath will never grow to the next stage, for design purposes.")
					.define("enableIcePreventsCropGrowth", false);
			enableInfiniteTrading = builder
					.comment("If Infinite Trading should be enabled. Prevents villager trades from locking up, making them be always available.")
					.define("enableInfiniteTrading", false);
			enableInventoryTotem = builder
					.comment("If Inventory Totem should be enabled. Makes the totem of undying effect work when existing in the player's inventory.")
					.define("enableInventoryTotem", false);
			enableJustMobHeads = builder
					.comment("If Just Mob Heads should be enabled. A minimalistic mod that adds a configurable chance for mobs to drop their respective heads on death.")
					.define("enableJustMobHeads", false);
			enableJustPlayerHeads = builder
					.comment("If Just Player Heads should be enabled. A minimalistic mod that adds a configurable chance for players to drop their respective heads on death and via a command. All heads are generated with texture-data.")
					.define("enableJustPlayerHeads", false);
			enableKelpFertilizer = builder
					.comment("If Kelp Fertilizer should be enabled. Kelp seaweed has a cell structure that filters sea water for rich nutrients. It can now act as a fertilizer like bone meal.")
					.define("enableKelpFertilizer", false);
			enableMilkAllTheMobs = builder
					.comment("If Milk All The Mobs should be enabled. Allows sheep, llamas, pigs, donkeys, horses and mules to produce a bucket of milk.")
					.define("enableMilkAllTheMobs", false);
			enableMineralChance = builder
					.comment("If Mineral Chance should be enabled. Adds a chance to find a random mineral inside a stone block when mining.")
					.define("enableMineralChance", false);
			enableMooshroomSpawn = builder
					.comment("If Mooshroom Spawn should be enabled. Adds a chance for mooshrooms to spawn naturally everywhere in place of a cow.")
					.define("enableMooshroomSpawn", false);
			enableMooshroomTweaks = builder
					.comment("If Mooshroom Tweaks should be enabled. A minimalistic mod that tweaks the spawning behaviour of mooshrooms, allowing both red and brown to spawn naturally.")
					.define("enableMooshroomTweaks", false);
			enableMoreZombieVillagers = builder
					.comment("If More Zombie Villagers should be enabled. Allows modifying the default chance a zombie spawn is of the villager variant.")
					.define("enableMoreZombieVillagers", false);
			enableMoveBoats = builder
					.comment("If Move Boats should be enabled. Allows players to easily move boats without breaking them by holding right click.")
					.define("enableMoveBoats", false);
			enableMoveMinecarts = builder
					.comment("If Move Minecarts should be enabled. Allows players to easily pick up and move minecarts without breaking them by holding right click.")
					.define("enableMoveMinecarts", false);
			enableNameTagTweaks = builder
					.comment("If Name Tag Tweaks should be enabled. Adds name tag crafting recipe, /nametag command, and named entities drop their name tag on death.")
					.define("enableNameTagTweaks", false);
			enableNaturallyChargedCreepers = builder
					.comment("If Naturally Charged Creepers should be enabled. A minimalistic mod that allows creepers to spawn charged, both naturally and from a spawn egg.")
					.define("enableNaturallyChargedCreepers", false);
			enableNetherPortalSpread = builder
					.comment("If Nether Portal Spread should be enabled. Nether portals spread (custom) nether blocks to the overworld around the portal in a configurable radius.")
					.define("enableNetherPortalSpread", false);
			enableNoHostilesAroundCampfire = builder
					.comment("If No Hostiles Around Campfire should be enabled. Prevents hostile mob spawns around the campfire in a configurable radius.")
					.define("enableNoHostilesAroundCampfire", false);
			enableNutritiousMilk = builder
					.comment("If Nutritious Milk should be enabled. Adds a nutritious value to milk, filling up the food bar and saturation when consumed.")
					.define("enableNutritiousMilk", false);
			enableOmegaMute = builder
					.comment("If Omega Mute should be enabled. [Client] The last mod you'll ever need for culling and silencing Minecraft sounds while in-game or via a file.")
					.define("enableOmegaMute", false);
			enablePaperBooks = builder
					.comment("If Paper Books should be enabled. Allows the creation of books with 9 paper.")
					.define("enablePaperBooks", false);
			enablePassiveEndermen = builder
					.comment("If Passive Endermen should be enabled. Configurably prevent an enderman from picking up blocks, griefing and attacking players.")
					.define("enablePassiveEndermen", false);
			enablePassiveShield = builder
					.comment("If Passive Shield should be enabled. The shield is hidden in the offhand until right-clicked, and shields passively negate damage when held but not in use.")
					.define("enablePassiveShield", false);
			enablePetNames = builder
					.comment("If Pet Names should be enabled. Gives tamable baby animals a pre-defined name. For Wolves, Cats, Horses, Donkeys, Mules and Llamas.")
					.define("enablePetNames", false);
			enableQuickPaths = builder
					.comment("If Quick Paths should be enabled. Create long paths instantly by setting a start and end point.")
					.define("enableQuickPaths", false);
			enableRainBeGoneRitual = builder
					.comment("If Rain Be Gone Ritual should be enabled. Adds the ability to stop the rain with a multi-block structure ritual.")
					.define("enableRainBeGoneRitual", false);
			enableRandomBoneMealFlowers = builder
					.comment("If Random Bone Meal Flowers should be enabled. Randomizes the flowers spawned by bone meal, allowing all (modded) types to spawn everywhere.")
					.define("enableRandomBoneMealFlowers", false);
			enableRandomSheepColours = builder
					.comment("If Random Sheep Colours should be enabled. Gives new sheep spawns a random wool colour based on a configurable list.")
					.define("enableRandomSheepColours", false);
			enableRandomShulkerColours = builder
					.comment("If Random Shulker Colours should be enabled. Gives new shulker spawns a random shell colour based on a configurable list.")
					.define("enableRandomShulkerColours", false);
			enableRandomVillageNames = builder
					.comment("If Random Village Names should be enabled. Gives all villages a random name from a preset list. Uses the Areas mod to display the name in the GUI.")
					.define("enableRandomVillageNames", false);
			enableRealisticBees = builder
					.comment("If Realistic Bees should be enabled. Makes bees more realistic without adding new objects. Bees have a chance to lose their stinger when stinging a player or entity, and will die after doing so. Stingers can be stuck in players and will need shears to be taken out to prevent further poisoning.")
					.define("enableRealisticBees", false);
			enableRecast = builder
					.comment("If Recast should be enabled. Automatically re-casts the fishing rod for a player when they catch an item.")
					.define("enableRecast", false);
			enableRecipeCommands = builder
					.comment("If Recipe Commands should be enabled. Adds the ability to receive the pattern and ingredients of any recipe via chat by a command.")
					.define("enableRecipeCommands", false);
			enableReplantingCrops = builder
					.comment("If Replanting Crops should be enabled. Automatically replants/replaces/reseeds crops harvested with seeds from drops, negated by holding the sneak button.")
					.define("enableReplantingCrops", false);
			enableRespawningShulkers = builder
					.comment("If Respawning Shulkers should be enabled. Shulkers will respawn on their death location after a configurable delay to make them more sustainable.")
					.define("enableRespawningShulkers", false);
			enableScaffoldingDropsNearby = builder
					.comment("If Scaffolding Drops Nearby should be enabled. When breaking a scaffolding block, all chained blocks will drop at the first block's position.")
					.define("enableScaffoldingDropsNearby", false);
			enableShulkerDropsTwo = builder
					.comment("If Shulker Drops Two should be enabled. A minimalistic configurable mod that allows shulkers to drop two or more shells and ignore their default drop chance.")
					.define("enableShulkerDropsTwo", false);
			enableSkeletonHorseSpawn = builder
					.comment("If Skeleton Horse Spawn should be enabled. Allows the skeleton horse to spawn naturally with a skeleton riding it and other tweaks.")
					.define("enableSkeletonHorseSpawn", false);
			enableSleepSooner = builder
					.comment("If Sleep Sooner should be enabled. A minimalistic mod that adds a configurable functionality to be able to sleep a little sooner.")
					.define("enableSleepSooner", false);
			enableSmallerNetherPortals = builder
					.comment("If Smaller Nether Portals should be enabled. Allows the creation of smaller nether portals, specifically 1x2, 1x3 and 2x2.")
					.define("enableSmallerNetherPortals", false);
			enableSnowballsFreezeMobs = builder
					.comment("If Snowballs Freeze Mobs should be enabled. Hitting mobs with a snowball will result in them being frozen for a configurable amount of time.")
					.define("enableSnowballsFreezeMobs", false);
			enableSofterHayBales = builder
					.comment("If Softer Hay Bales should be enabled. Makes hay bales negate fall damage entirely, Assassin's Creed style.")
					.define("enableSofterHayBales", false);
			enableSpidersProduceWebs = builder
					.comment("If Spiders Produce Webs should be enabled. Spiders and cave spiders can periodically produce a web when a player is close.")
					.define("enableSpidersProduceWebs", false);
			enableStackRefill = builder
					.comment("If Stack Refill should be enabled. Automatically refills the item in the hand of the player when using the final item if possible.")
					.define("enableStackRefill", false);
			enableStarterKit = builder
					.comment("If Starter Kit should be enabled. Gives players who join the world for the first time configurable starter gear.")
					.define("enableStarterKit", false);
			enableStraySpawn = builder
					.comment("If Stray Spawn should be enabled. Allows a chance for strays to spawn naturally everywhere instead of a skeleton.")
					.define("enableStraySpawn", false);
			enableSuperflatWorldNoSlimes = builder
					.comment("If Superflat World No Slimes should be enabled. Removes those pesky slimes when you're trying to be creative in a superflat world.")
					.define("enableSuperflatWorldNoSlimes", false);
			enableTNTBreaksBedrock = builder
					.comment("If TNT Breaks Bedrock should be enabled. Allows TNT to break bedrock. Now you've finally got easy access to the nether roof.")
					.define("enableTNTBreaksBedrock", false);
			enableTranscendingTrident = builder
					.comment("If Transcending Trident should be enabled. Improves the vanilla trident. Hold a water bucket to use riptide, which is more powerful.")
					.define("enableTranscendingTrident", false);
			enableTreeHarvester = builder
					.comment("If Tree Harvester should be enabled. Harvest trees instantly by chopping down the bottom block with fast leaf decay and sapling replacement.")
					.define("enableTreeHarvester", false);
			enableUnderwaterEnchanting = builder
					.comment("If Underwater Enchanting should be enabled. Allows the use of the enchantment table under water with maximum levels via bookshelves.")
					.define("enableUnderwaterEnchanting", false);
			enableVillageBellRecipe = builder
					.comment("If Village Bell Recipe should be enabled. Adds a recipe for the village bell item.")
					.define("enableVillageBellRecipe", false);
			enableVillageSpawnPoint = builder
					.comment("If Village Spawn Point should be enabled. Sets the spawn point of a new world to always be in the center of a village.")
					.define("enableVillageSpawnPoint", false);
			enableVillagerDeathMessages = builder
					.comment("If Villager Death Messages should be enabled. Broadcasts an informational death message whenever a villager passes away.")
					.define("enableVillagerDeathMessages", false);
			enableVillagerNames = builder
					.comment("If Villager Names should be enabled. A minimalistic mod which gives all unnamed villagers a (pre-)defined name.")
					.define("enableVillagerNames", false);
			enableWeakerSpiderwebs = builder
					.comment("If Weaker Spiderwebs should be enabled. Breaks spider webs/cobwebs with a configurable delay when walking through.")
					.define("enableWeakerSpiderwebs", false);
			enableWoolTweaks = builder
					.comment("If Wool Tweaks should be enabled. Dye beds, wool blocks and beds with a right-click in the world. Adds wool to string recipe for all wool blocks and allows players to dye any colour wool block with any dye.")
					.define("enableWoolTweaks", false);
			enableZombieHorseSpawn = builder
					.comment("If Zombie Horse Spawn should be enabled. Allows the zombie horse to spawn with a zombie riding it and other tweaks.")
					.define("enableZombieHorseSpawn", false);
			enableZombieProofDoors = builder
					.comment("If Zombie Proof Doors should be enabled. Prevents zombies from targeting and breaking down doors.")
					.define("enableZombieProofDoors", false);
			enableZombieVillagersFromSpawner = builder
					.comment("If Zombie Villagers From Spawner should be enabled. Puts back the ability for zombie spawners to spawn the villager variant, removed in 1.11.")
					.define("enableZombieVillagersFromSpawner", false);

			builder.pop();
		}
	}
}