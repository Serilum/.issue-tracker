/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.3.
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

package com.natamus.thevanillaexperience;

import com.natamus.collective.check.RegisterMod;
import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.collective.objects.SAMObject;
import com.natamus.thevanillaexperience.config.ConfigHandler;
import com.natamus.thevanillaexperience.events.TveRegisterEvent;
import com.natamus.thevanillaexperience.mods.advancementscreenshot.config.AdvancementScreenshotConfigHandler;
import com.natamus.thevanillaexperience.mods.advancementscreenshot.events.AdvancementScreenshotAdvancementGetEvent;
import com.natamus.thevanillaexperience.mods.alllootdrops.config.AllLootDropsConfigHandler;
import com.natamus.thevanillaexperience.mods.alllootdrops.events.AllLootDropsEntityEvent;
import com.natamus.thevanillaexperience.mods.alwaysawitherskull.events.AlwaysaWitherSkullEntityEvent;
import com.natamus.thevanillaexperience.mods.areas.config.AreasConfigHandler;
import com.natamus.thevanillaexperience.mods.areas.events.AreasAreaEvent;
import com.natamus.thevanillaexperience.mods.areas.events.AreasGUIEvent;
import com.natamus.thevanillaexperience.mods.areas.network.PacketToClientShowGUI;
import com.natamus.thevanillaexperience.mods.areas.util.AreasUtil;
import com.natamus.thevanillaexperience.mods.automaticdoors.config.AutomaticDoorsConfigHandler;
import com.natamus.thevanillaexperience.mods.automaticdoors.events.AutomaticDoorsDoorEvent;
import com.natamus.thevanillaexperience.mods.betterbeaconplacement.config.BetterBeaconPlacementConfigHandler;
import com.natamus.thevanillaexperience.mods.betterbeaconplacement.events.BetterBeaconPlacementBeaconEvent;
import com.natamus.thevanillaexperience.mods.betterconduitplacement.config.BetterConduitPlacementConfigHandler;
import com.natamus.thevanillaexperience.mods.betterconduitplacement.events.BetterConduitPlacementConduitEvent;
import com.natamus.thevanillaexperience.mods.betterspawnercontrol.events.BetterSpawnerControlMobSpawnEvent;
import com.natamus.thevanillaexperience.mods.biggerspongeabsorptionradius.blocks.ExtendedSpongeBlock;
import com.natamus.thevanillaexperience.mods.biggerspongeabsorptionradius.util.BiggerSpongeAbsorptionRadiusVariables;
import com.natamus.thevanillaexperience.mods.bottleyourxp.config.BottleYourXpConfigHandler;
import com.natamus.thevanillaexperience.mods.bottleyourxp.events.BottleYourXpClickEvent;
import com.natamus.thevanillaexperience.mods.breedablekillerrabbit.config.BreedableKillerRabbitConfigHandler;
import com.natamus.thevanillaexperience.mods.breedablekillerrabbit.events.BreedableKillerRabbitEntityEvent;
import com.natamus.thevanillaexperience.mods.campfirespawnandtweaks.config.CampfireSpawnandTweaksConfigHandler;
import com.natamus.thevanillaexperience.mods.campfirespawnandtweaks.events.CampfireSpawnandTweaksCampfireEvent;
import com.natamus.thevanillaexperience.mods.conduitspreventdrowned.config.ConduitsPreventDrownedConfigHandler;
import com.natamus.thevanillaexperience.mods.conduitspreventdrowned.events.ConduitsPreventDrownedDrownedEvent;
import com.natamus.thevanillaexperience.mods.configurabledespawntimer.config.ConfigurableDespawnTimerConfigHandler;
import com.natamus.thevanillaexperience.mods.configurabledespawntimer.events.ConfigurableDespawnTimerItemEvent;
import com.natamus.thevanillaexperience.mods.creativeblockreplacer.events.CreativeBlockReplacerReplaceEvent;
import com.natamus.thevanillaexperience.mods.cryingghasts.config.CryingGhastsConfigHandler;
import com.natamus.thevanillaexperience.mods.cryingghasts.events.CryingGhastsGhastEvent;
import com.natamus.thevanillaexperience.mods.cyclepaintings.events.CyclePaintingsPaintingEvent;
import com.natamus.thevanillaexperience.mods.cyclepaintings.util.CyclePaintingsUtil;
import com.natamus.thevanillaexperience.mods.deathbackup.config.DeathBackupConfigHandler;
import com.natamus.thevanillaexperience.mods.deathbackup.events.DeathBackupDeathBackupEvent;
import com.natamus.thevanillaexperience.mods.despawningeggshatch.config.DespawningEggsHatchConfigHandler;
import com.natamus.thevanillaexperience.mods.despawningeggshatch.events.DespawningEggsHatchEggEvent;
import com.natamus.thevanillaexperience.mods.dismountentity.events.DismountEntityDismountEvent;
import com.natamus.thevanillaexperience.mods.doubledoors.config.DoubleDoorsConfigHandler;
import com.natamus.thevanillaexperience.mods.doubledoors.events.DoubleDoorsDoorEvent;
import com.natamus.thevanillaexperience.mods.dragondropselytra.events.DragonDropsElytraDragonEvent;
import com.natamus.thevanillaexperience.mods.easyelytratakeoff.events.EasyElytraTakeoffElytraEvent;
import com.natamus.thevanillaexperience.mods.edibles.config.EdiblesConfigHandler;
import com.natamus.thevanillaexperience.mods.edibles.events.EdiblesEdibleEvent;
import com.natamus.thevanillaexperience.mods.enchantingcommands.config.EnchantingCommandsConfigHandler;
import com.natamus.thevanillaexperience.mods.endportalrecipe.config.EndPortalRecipeConfigHandler;
import com.natamus.thevanillaexperience.mods.endportalrecipe.events.EndPortalRecipeEndPortalEvent;
import com.natamus.thevanillaexperience.mods.erodingstoneentities.config.ErodingStoneEntitiesConfigHandler;
import com.natamus.thevanillaexperience.mods.erodingstoneentities.events.ErodingStoneEntitiesEntityEvent;
import com.natamus.thevanillaexperience.mods.erodingstoneentities.util.ErodingStoneEntitiesUtil;
import com.natamus.thevanillaexperience.mods.extendedbonemeal.events.ExtendedBoneMealExtendedEvent;
import com.natamus.thevanillaexperience.mods.extendedcreativeinventory.config.ExtendedCreativeInventoryConfigHandler;
import com.natamus.thevanillaexperience.mods.extendedcreativeinventory.itemgroups.ExtendedItemGroup;
import com.natamus.thevanillaexperience.mods.extendedcreativeinventory.util.ExtendedCreativeInventoryVariables;
import com.natamus.thevanillaexperience.mods.extractpoison.config.ExtractPoisonConfigHandler;
import com.natamus.thevanillaexperience.mods.extractpoison.events.ExtractPoisonEntityEvent;
import com.natamus.thevanillaexperience.mods.fallthroughslime.events.FallThroughSlimeSlimeEvent;
import com.natamus.thevanillaexperience.mods.firespreadtweaks.config.FireSpreadTweaksConfigHandler;
import com.natamus.thevanillaexperience.mods.firespreadtweaks.events.FireSpreadTweaksFireSpreadEvent;
import com.natamus.thevanillaexperience.mods.firstjoinmessage.config.FirstJoinMessageConfigHandler;
import com.natamus.thevanillaexperience.mods.firstjoinmessage.events.FirstJoinMessageFirstSpawnEvent;
import com.natamus.thevanillaexperience.mods.fishontheline.config.FishOnTheLineConfigHandler;
import com.natamus.thevanillaexperience.mods.fishontheline.events.FishOnTheLineFishOnTheLineEvent;
import com.natamus.thevanillaexperience.mods.fixedanvilrepaircost.config.FixedAnvilRepairCostConfigHandler;
import com.natamus.thevanillaexperience.mods.fixedanvilrepaircost.events.FixedAnvilRepairCostRepairEvent;
import com.natamus.thevanillaexperience.mods.giantspawn.config.GiantSpawnConfigHandler;
import com.natamus.thevanillaexperience.mods.giantspawn.events.GiantSpawnGiantEvent;
import com.natamus.thevanillaexperience.mods.grassseeds.events.GrassSeedsGrassEvent;
import com.natamus.thevanillaexperience.mods.grindstonesharpertools.config.GrindstoneSharperToolsConfigHandler;
import com.natamus.thevanillaexperience.mods.grindstonesharpertools.events.GrindstoneSharperToolsGrindEvent;
import com.natamus.thevanillaexperience.mods.guiclock.config.GUIClockConfigHandler;
import com.natamus.thevanillaexperience.mods.guiclock.events.GUIClockGUIEvent;
import com.natamus.thevanillaexperience.mods.guicompass.config.GUICompassConfigHandler;
import com.natamus.thevanillaexperience.mods.guicompass.events.GUICompassGUIEvent;
import com.natamus.thevanillaexperience.mods.guifollowers.config.GUIFollowersConfigHandler;
import com.natamus.thevanillaexperience.mods.guifollowers.events.GUIFollowersFollowerEvent;
import com.natamus.thevanillaexperience.mods.guifollowers.events.GUIFollowersGUIEvent;
import com.natamus.thevanillaexperience.mods.guifollowers.util.GUIFollowersVariables;
import com.natamus.thevanillaexperience.mods.handoveryouritems.config.HandOverYourItemsConfigHandler;
import com.natamus.thevanillaexperience.mods.handoveryouritems.events.HandOverYourItemsHandOverEvent;
import com.natamus.thevanillaexperience.mods.healingcampfire.config.HealingCampfireConfigHandler;
import com.natamus.thevanillaexperience.mods.healingcampfire.events.HealingCampfireCampfireEvent;
import com.natamus.thevanillaexperience.mods.hidehands.config.HideHandsConfigHandler;
import com.natamus.thevanillaexperience.mods.hidehands.events.HideHandsHandEvent;
import com.natamus.thevanillaexperience.mods.huskspawn.config.HuskSpawnConfigHandler;
import com.natamus.thevanillaexperience.mods.icepreventscropgrowth.events.IcePreventsCropGrowthCropEvent;
import com.natamus.thevanillaexperience.mods.infinitetrading.config.InfiniteTradingConfigHandler;
import com.natamus.thevanillaexperience.mods.infinitetrading.events.InfiniteTradingVillagerEvent;
import com.natamus.thevanillaexperience.mods.inventorytotem.events.InventoryTotemTotemEvent;
import com.natamus.thevanillaexperience.mods.justmobheads.config.JustMobHeadsConfigHandler;
import com.natamus.thevanillaexperience.mods.justmobheads.events.JustMobHeadsHeadDropEvent;
import com.natamus.thevanillaexperience.mods.justmobheads.util.HeadData;
import com.natamus.thevanillaexperience.mods.justplayerheads.config.JustPlayerHeadsConfigHandler;
import com.natamus.thevanillaexperience.mods.justplayerheads.events.JustPlayerHeadsPlayerEvent;
import com.natamus.thevanillaexperience.mods.kelpfertilizer.dispenser.RecipeManager;
import com.natamus.thevanillaexperience.mods.kelpfertilizer.events.KelpFertilizerKelpEvent;
import com.natamus.thevanillaexperience.mods.milkallthemobs.events.MilkAllTheMobsMilkEvent;
import com.natamus.thevanillaexperience.mods.mineralchance.config.MineralChanceConfigHandler;
import com.natamus.thevanillaexperience.mods.mineralchance.events.MineralChanceMiningEvent;
import com.natamus.thevanillaexperience.mods.mooshroomspawn.config.MooshroomSpawnConfigHandler;
import com.natamus.thevanillaexperience.mods.mooshroomtweaks.config.MooshroomTweaksConfigHandler;
import com.natamus.thevanillaexperience.mods.mooshroomtweaks.events.MooshroomTweaksMooshroomEvent;
import com.natamus.thevanillaexperience.mods.morezombievillagers.config.MoreZombieVillagersConfigHandler;
import com.natamus.thevanillaexperience.mods.moveboats.config.MoveBoatsConfigHandler;
import com.natamus.thevanillaexperience.mods.moveboats.events.MoveBoatsBoatEvent;
import com.natamus.thevanillaexperience.mods.moveminecarts.config.MoveMinecartsConfigHandler;
import com.natamus.thevanillaexperience.mods.moveminecarts.events.MoveMinecartsMinecartEvent;
import com.natamus.thevanillaexperience.mods.nametagtweaks.config.NameTagTweaksConfigHandler;
import com.natamus.thevanillaexperience.mods.nametagtweaks.events.NameTagTweaksNameTagEvent;
import com.natamus.thevanillaexperience.mods.naturallychargedcreepers.config.NaturallyChargedCreepersConfigHandler;
import com.natamus.thevanillaexperience.mods.naturallychargedcreepers.events.NaturallyChargedCreepersEntityEvent;
import com.natamus.thevanillaexperience.mods.netherportalspread.config.NetherPortalSpreadConfigHandler;
import com.natamus.thevanillaexperience.mods.netherportalspread.events.NetherPortalSpreadSpreadEvent;
import com.natamus.thevanillaexperience.mods.netherportalspread.util.NetherPortalSpreadUtil;
import com.natamus.thevanillaexperience.mods.nohostilesaroundcampfire.config.NoHostilesAroundCampfireConfigHandler;
import com.natamus.thevanillaexperience.mods.nohostilesaroundcampfire.events.NoHostilesAroundCampfireCampfireEvent;
import com.natamus.thevanillaexperience.mods.nutritiousmilk.config.NutritiousMilkConfigHandler;
import com.natamus.thevanillaexperience.mods.nutritiousmilk.events.NutritiousMilkMilkEvent;
import com.natamus.thevanillaexperience.mods.omegamute.events.OmegaMuteMuteEvent;
import com.natamus.thevanillaexperience.mods.omegamute.util.OmegaMuteUtil;
import com.natamus.thevanillaexperience.mods.omegamute.util.OmegaMuteVariables;
import com.natamus.thevanillaexperience.mods.passiveendermen.config.PassiveEndermenConfigHandler;
import com.natamus.thevanillaexperience.mods.passiveendermen.events.PassiveEndermenEndermenEvent;
import com.natamus.thevanillaexperience.mods.passiveshield.config.PassiveShieldConfigHandler;
import com.natamus.thevanillaexperience.mods.passiveshield.events.PassiveShieldClientEvent;
import com.natamus.thevanillaexperience.mods.passiveshield.events.PassiveShieldServerEvent;
import com.natamus.thevanillaexperience.mods.petnames.config.PetNamesConfigHandler;
import com.natamus.thevanillaexperience.mods.petnames.events.PetNamesEntityEvent;
import com.natamus.thevanillaexperience.mods.quickpaths.events.QuickPathsPathEvent;
import com.natamus.thevanillaexperience.mods.rainbegoneritual.events.RainBeGoneRitualRitualEvent;
import com.natamus.thevanillaexperience.mods.randombonemealflowers.events.RandomBoneMealFlowersFlowerEvent;
import com.natamus.thevanillaexperience.mods.randombonemealflowers.util.RandomBoneMealFlowersUtil;
import com.natamus.thevanillaexperience.mods.randomsheepcolours.config.RandomSheepColoursConfigHandler;
import com.natamus.thevanillaexperience.mods.randomsheepcolours.events.RandomSheepColoursSheepEvent;
import com.natamus.thevanillaexperience.mods.randomsheepcolours.util.RandomSheepColoursUtil;
import com.natamus.thevanillaexperience.mods.randomshulkercolours.config.RandomShulkerColoursConfigHandler;
import com.natamus.thevanillaexperience.mods.randomshulkercolours.events.RandomShulkerColoursShulkerEvent;
import com.natamus.thevanillaexperience.mods.randomshulkercolours.util.RandomShulkerColoursUtil;
import com.natamus.thevanillaexperience.mods.randomvillagenames.events.RandomVillageNamesSetVillageSignEvent;
import com.natamus.thevanillaexperience.mods.realisticbees.config.RealisticBeesConfigHandler;
import com.natamus.thevanillaexperience.mods.realisticbees.events.RealisticBeesBeeEvent;
import com.natamus.thevanillaexperience.mods.recast.events.RecastRecastEvent;
import com.natamus.thevanillaexperience.mods.replantingcrops.config.ReplantingCropsConfigHandler;
import com.natamus.thevanillaexperience.mods.replantingcrops.events.ReplantingCropsCropEvent;
import com.natamus.thevanillaexperience.mods.respawningshulkers.config.RespawningShulkersConfigHandler;
import com.natamus.thevanillaexperience.mods.respawningshulkers.events.RespawningShulkersShulkerEvent;
import com.natamus.thevanillaexperience.mods.scaffoldingdropsnearby.events.ScaffoldingDropsNearbyScaffoldingEvent;
import com.natamus.thevanillaexperience.mods.shulkerdropstwo.config.ShulkerDropsTwoConfigHandler;
import com.natamus.thevanillaexperience.mods.shulkerdropstwo.events.ShulkerDropsTwoEntityEvent;
import com.natamus.thevanillaexperience.mods.skeletonhorsespawn.config.SkeletonHorseSpawnConfigHandler;
import com.natamus.thevanillaexperience.mods.skeletonhorsespawn.events.SkeletonHorseSpawnSkeletonHorseEvent;
import com.natamus.thevanillaexperience.mods.sleepsooner.config.SleepSoonerConfigHandler;
import com.natamus.thevanillaexperience.mods.sleepsooner.events.SleepSoonerPlayerEvent;
import com.natamus.thevanillaexperience.mods.smallernetherportals.events.SmallerNetherPortalsPortalEvent;
import com.natamus.thevanillaexperience.mods.snowballsfreezemobs.config.SnowballsFreezeMobsConfigHandler;
import com.natamus.thevanillaexperience.mods.snowballsfreezemobs.events.SnowballsFreezeMobsSnowEvent;
import com.natamus.thevanillaexperience.mods.softerhaybales.events.SofterHayBalesFallEvent;
import com.natamus.thevanillaexperience.mods.spidersproducewebs.config.SpidersProduceWebsConfigHandler;
import com.natamus.thevanillaexperience.mods.spidersproducewebs.events.SpidersProduceWebsSpiderEvent;
import com.natamus.thevanillaexperience.mods.stackrefill.events.StackRefillRefillEvent;
import com.natamus.thevanillaexperience.mods.starterkit.events.StarterKitFirstSpawnEvent;
import com.natamus.thevanillaexperience.mods.starterkit.util.StarterKitUtil;
import com.natamus.thevanillaexperience.mods.strayspawn.config.StraySpawnConfigHandler;
import com.natamus.thevanillaexperience.mods.superflatworldnoslimes.events.SuperflatWorldNoSlimesSlimeEvent;
import com.natamus.thevanillaexperience.mods.tntbreaksbedrock.events.TNTBreaksBedrockBoomEvent;
import com.natamus.thevanillaexperience.mods.transcendingtrident.config.TranscendingTridentConfigHandler;
import com.natamus.thevanillaexperience.mods.transcendingtrident.events.TranscendingTridentTridentEvent;
import com.natamus.thevanillaexperience.mods.transcendingtrident.items.ExtendedTridentItem;
import com.natamus.thevanillaexperience.mods.treeharvester.config.TreeHarvesterConfigHandler;
import com.natamus.thevanillaexperience.mods.treeharvester.events.TreeHarvesterSoundHarvestEvent;
import com.natamus.thevanillaexperience.mods.treeharvester.events.TreeHarvesterTreeEvent;
import com.natamus.thevanillaexperience.mods.underwaterenchanting.events.UnderwaterEnchantingEnhantmentEvent;
import com.natamus.thevanillaexperience.mods.villagerdeathmessages.config.VillagerDeathMessagesConfigHandler;
import com.natamus.thevanillaexperience.mods.villagerdeathmessages.events.VillagerDeathMessagesVillagerEvent;
import com.natamus.thevanillaexperience.mods.villagernames.config.VillagerNamesConfigHandler;
import com.natamus.thevanillaexperience.mods.villagernames.events.VillagerNamesVillagerEvent;
import com.natamus.thevanillaexperience.mods.villagernames.util.Names;
import com.natamus.thevanillaexperience.mods.villagespawnpoint.events.VillageSpawnPointVillageSpawnEvent;
import com.natamus.thevanillaexperience.mods.weakerspiderwebs.config.WeakerSpiderwebsConfigHandler;
import com.natamus.thevanillaexperience.mods.weakerspiderwebs.events.WeakerSpiderwebsWebEvent;
import com.natamus.thevanillaexperience.mods.wooltweaks.events.WoolTweaksWoolClickEvent;
import com.natamus.thevanillaexperience.mods.wooltweaks.util.WoolTweaksUtil;
import com.natamus.thevanillaexperience.mods.zombiehorsespawn.config.ZombieHorseSpawnConfigHandler;
import com.natamus.thevanillaexperience.mods.zombiehorsespawn.events.ZombieHorseSpawnZombieHorseEvent;
import com.natamus.thevanillaexperience.mods.zombieproofdoors.events.ZombieProofDoorsZombieJoinEvent;
import com.natamus.thevanillaexperience.mods.zombievillagersfromspawner.config.ZombieVillagersFromSpawnerConfigHandler;
import com.natamus.thevanillaexperience.util.Reference;
import com.natamus.thevanillaexperience.util.TveUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
	public Main() {
		instance = this;

		ModLoadingContext modLoadingContext = ModLoadingContext.get();
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		MinecraftForge.EVENT_BUS.register(this);
		
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::initClient);
		modEventBus.addListener(this::loadComplete);

		String dirpath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "TVE";
		File dir = new File(dirpath);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec, "thevanillaexperience-common.toml");

		modLoadingContext.registerConfig(ModConfig.Type.COMMON, AdvancementScreenshotConfigHandler.spec, "TVE" + File.separator + "advancementscreenshot-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, AllLootDropsConfigHandler.spec, "TVE" + File.separator + "alllootdrops-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, AreasConfigHandler.spec, "TVE" + File.separator + "areas-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, AutomaticDoorsConfigHandler.spec, "TVE" + File.separator + "automaticdoors-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, BetterBeaconPlacementConfigHandler.spec, "TVE" + File.separator + "betterbeaconplacement-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, BetterConduitPlacementConfigHandler.spec, "TVE" + File.separator + "betterconduitplacement-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, BottleYourXpConfigHandler.spec, "TVE" + File.separator + "bottleyourxp-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, BreedableKillerRabbitConfigHandler.spec, "TVE" + File.separator + "breedablekillerrabbit-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, CampfireSpawnandTweaksConfigHandler.spec, "TVE" + File.separator + "campfirespawnandtweaks-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConduitsPreventDrownedConfigHandler.spec, "TVE" + File.separator + "conduitspreventdrowned-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigurableDespawnTimerConfigHandler.spec, "TVE" + File.separator + "configurabledespawntimer-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, CryingGhastsConfigHandler.spec, "TVE" + File.separator + "cryingghasts-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, DeathBackupConfigHandler.spec, "TVE" + File.separator + "deathbackup-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, DespawningEggsHatchConfigHandler.spec, "TVE" + File.separator + "despawningeggshatch-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, DoubleDoorsConfigHandler.spec, "TVE" + File.separator + "doubledoors-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, EdiblesConfigHandler.spec, "TVE" + File.separator + "edibles-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, EnchantingCommandsConfigHandler.spec, "TVE" + File.separator + "enchantingcommands-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, EndPortalRecipeConfigHandler.spec, "TVE" + File.separator + "endportalrecipe-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ErodingStoneEntitiesConfigHandler.spec, "TVE" + File.separator + "erodingstoneentities-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ExtendedCreativeInventoryConfigHandler.spec, "TVE" + File.separator + "extendedcreativeinventory-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ExtractPoisonConfigHandler.spec, "TVE" + File.separator + "extractpoison-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, FireSpreadTweaksConfigHandler.spec, "TVE" + File.separator + "firespreadtweaks-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, FirstJoinMessageConfigHandler.spec, "TVE" + File.separator + "firstjoinmessage-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, FishOnTheLineConfigHandler.spec, "TVE" + File.separator + "fishontheline-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, FixedAnvilRepairCostConfigHandler.spec, "TVE" + File.separator + "fixedanvilrepaircost-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, GiantSpawnConfigHandler.spec, "TVE" + File.separator + "giantspawn-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, GrindstoneSharperToolsConfigHandler.spec, "TVE" + File.separator + "grindstonesharpertools-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, GUIClockConfigHandler.spec, "TVE" + File.separator + "guiclock-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, GUICompassConfigHandler.spec, "TVE" + File.separator + "guicompass-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, GUIFollowersConfigHandler.spec, "TVE" + File.separator + "guifollowers-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, HandOverYourItemsConfigHandler.spec, "TVE" + File.separator + "handoveryouritems-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, HealingCampfireConfigHandler.spec, "TVE" + File.separator + "healingcampfire-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, HideHandsConfigHandler.spec, "TVE" + File.separator + "hidehands-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, HuskSpawnConfigHandler.spec, "TVE" + File.separator + "huskspawn-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, InfiniteTradingConfigHandler.spec, "TVE" + File.separator + "infinitetrading-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, JustMobHeadsConfigHandler.spec, "TVE" + File.separator + "justmobheads-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, JustPlayerHeadsConfigHandler.spec, "TVE" + File.separator + "justplayerheads-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, MineralChanceConfigHandler.spec, "TVE" + File.separator + "mineralchance-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, MooshroomSpawnConfigHandler.spec, "TVE" + File.separator + "mooshroomspawn-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, MooshroomTweaksConfigHandler.spec, "TVE" + File.separator + "mooshroomtweaks-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, MoreZombieVillagersConfigHandler.spec, "TVE" + File.separator + "morezombievillagers-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, MoveBoatsConfigHandler.spec, "TVE" + File.separator + "moveboats-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, MoveMinecartsConfigHandler.spec, "TVE" + File.separator + "moveminecarts-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, NameTagTweaksConfigHandler.spec, "TVE" + File.separator + "nametagtweaks-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, NaturallyChargedCreepersConfigHandler.spec, "TVE" + File.separator + "naturallychargedcreepers-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, NetherPortalSpreadConfigHandler.spec, "TVE" + File.separator + "netherportalspread-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, NoHostilesAroundCampfireConfigHandler.spec, "TVE" + File.separator + "nohostilesaroundcampfire-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, NutritiousMilkConfigHandler.spec, "TVE" + File.separator + "nutritiousmilk-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, PassiveEndermenConfigHandler.spec, "TVE" + File.separator + "passiveendermen-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, PassiveShieldConfigHandler.spec, "TVE" + File.separator + "passiveshield-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, PetNamesConfigHandler.spec, "TVE" + File.separator + "petnames-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, RandomSheepColoursConfigHandler.spec, "TVE" + File.separator + "randomsheepcolours-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, RandomShulkerColoursConfigHandler.spec, "TVE" + File.separator + "randomshulkercolours-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, RealisticBeesConfigHandler.spec, "TVE" + File.separator + "realisticbees-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ReplantingCropsConfigHandler.spec, "TVE" + File.separator + "replantingcrops-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, RespawningShulkersConfigHandler.spec, "TVE" + File.separator + "respawningshulkers-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ShulkerDropsTwoConfigHandler.spec, "TVE" + File.separator + "shulkerdropstwo-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, SkeletonHorseSpawnConfigHandler.spec, "TVE" + File.separator + "skeletonhorsespawn-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, SleepSoonerConfigHandler.spec, "TVE" + File.separator + "sleepsooner-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, SnowballsFreezeMobsConfigHandler.spec, "TVE" + File.separator + "snowballsfreezemobs-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, SpidersProduceWebsConfigHandler.spec, "TVE" + File.separator + "spidersproducewebs-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, StraySpawnConfigHandler.spec, "TVE" + File.separator + "strayspawn-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, TranscendingTridentConfigHandler.spec, "TVE" + File.separator + "transcendingtrident-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, TreeHarvesterConfigHandler.spec, "TVE" + File.separator + "treeharvester-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, VillagerDeathMessagesConfigHandler.spec, "TVE" + File.separator + "villagerdeathmessages-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, VillagerNamesConfigHandler.spec, "TVE" + File.separator + "villagernames-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, WeakerSpiderwebsConfigHandler.spec, "TVE" + File.separator + "weakerspiderwebs-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ZombieHorseSpawnConfigHandler.spec, "TVE" + File.separator + "zombiehorsespawn-common.toml");
		modLoadingContext.registerConfig(ModConfig.Type.COMMON, ZombieVillagersFromSpawnerConfigHandler.spec, "TVE" + File.separator + "zombievillagersfromspawner-common.toml");

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}
	
	private void commonSetup(final FMLCommonSetupEvent e) {
			if ((ConfigHandler.GENERAL.enableAreas.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Areas")) {
			AreasUtil.network = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.MOD_ID, Reference.MOD_ID), () -> "1.0", s -> true, s -> true);
			AreasUtil.network.registerMessage(1, PacketToClientShowGUI.class, PacketToClientShowGUI::toBytes, PacketToClientShowGUI::new, PacketToClientShowGUI::handle);
		}
	}
	
	private void initClient(final FMLClientSetupEvent e) {
			if ((ConfigHandler.GENERAL.enableGUIFollowers.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("GUI Followers") && FMLEnvironment.dist.equals(Dist.CLIENT)) {
			GUIFollowersVariables.clearlist_hotkey = new KeyMapping("Clear Follower List", 92, "key.categories.misc");
			ClientRegistry.registerKeyBinding(GUIFollowersVariables.clearlist_hotkey);			
		}
			if ((ConfigHandler.GENERAL.enableOmegaMute.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Omega Mute") && FMLEnvironment.dist.equals(Dist.CLIENT)) {
			OmegaMuteVariables.hotkey = new KeyMapping("Reload Omega Mute config", 46, "key.categories.misc");
			ClientRegistry.registerKeyBinding(OmegaMuteVariables.hotkey);			
		}
	}
	
	private void loadComplete(final FMLLoadCompleteEvent e) {
			MinecraftForge.EVENT_BUS.register(new TveRegisterEvent());

			if ((ConfigHandler.GENERAL.enableAdvancementScreenshot.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Advancement Screenshot") && FMLEnvironment.dist.equals(Dist.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new AdvancementScreenshotAdvancementGetEvent());
		}
			if ((ConfigHandler.GENERAL.enableAllLootDrops.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("All Loot Drops")) {
			MinecraftForge.EVENT_BUS.register(new AllLootDropsEntityEvent());
		}
			if ((ConfigHandler.GENERAL.enableAlwaysaWitherSkull.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Always a Wither Skull")) {
			MinecraftForge.EVENT_BUS.register(new AlwaysaWitherSkullEntityEvent());
		}
			if ((ConfigHandler.GENERAL.enableAreas.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Areas")) {
			if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new AreasGUIEvent(Minecraft.getInstance()));
		}
			MinecraftForge.EVENT_BUS.register(new AreasAreaEvent());
		}
			if ((ConfigHandler.GENERAL.enableAutomaticDoors.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Automatic Doors")) {
			MinecraftForge.EVENT_BUS.register(new AutomaticDoorsDoorEvent());
		}
			if ((ConfigHandler.GENERAL.enableBetterBeaconPlacement.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Better Beacon Placement")) {
			MinecraftForge.EVENT_BUS.register(new BetterBeaconPlacementBeaconEvent());
		}
			if ((ConfigHandler.GENERAL.enableBetterConduitPlacement.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Better Conduit Placement")) {
			MinecraftForge.EVENT_BUS.register(new BetterConduitPlacementConduitEvent());
		}
			if ((ConfigHandler.GENERAL.enableBetterSpawnerControl.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Better Spawner Control")) {
			MinecraftForge.EVENT_BUS.register(new BetterSpawnerControlMobSpawnEvent());
		}
			if ((ConfigHandler.GENERAL.enableBottleYourXp.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Bottle Your Xp")) {
			MinecraftForge.EVENT_BUS.register(new BottleYourXpClickEvent());
		}
			if ((ConfigHandler.GENERAL.enableBreedableKillerRabbit.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Breedable Killer Rabbit")) {
			MinecraftForge.EVENT_BUS.register(new BreedableKillerRabbitEntityEvent());
		}
			if ((ConfigHandler.GENERAL.enableCampfireSpawnandTweaks.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Campfire Spawn and Tweaks")) {
			MinecraftForge.EVENT_BUS.register(new CampfireSpawnandTweaksCampfireEvent());
		}
			if ((ConfigHandler.GENERAL.enableConduitsPreventDrowned.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Conduits Prevent Drowned")) {
			MinecraftForge.EVENT_BUS.register(new ConduitsPreventDrownedDrownedEvent());
		}
			if ((ConfigHandler.GENERAL.enableConfigurableDespawnTimer.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Configurable Despawn Timer")) {
			MinecraftForge.EVENT_BUS.register(new ConfigurableDespawnTimerItemEvent());
		}
			if ((ConfigHandler.GENERAL.enableCreativeBlockReplacer.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Creative Block Replacer")) {
			MinecraftForge.EVENT_BUS.register(new CreativeBlockReplacerReplaceEvent());
		}
			if ((ConfigHandler.GENERAL.enableCryingGhasts.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Crying Ghasts")) {
			MinecraftForge.EVENT_BUS.register(new CryingGhastsGhastEvent());
		}
			if ((ConfigHandler.GENERAL.enableCyclePaintings.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Cycle Paintings")) {
			CyclePaintingsUtil.setPaintings();
			MinecraftForge.EVENT_BUS.register(new CyclePaintingsPaintingEvent());
		}
			if ((ConfigHandler.GENERAL.enableDeathBackup.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Death Backup")) {
			MinecraftForge.EVENT_BUS.register(new DeathBackupDeathBackupEvent());
		}
			if ((ConfigHandler.GENERAL.enableDespawningEggsHatch.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Despawning Eggs Hatch")) {
			MinecraftForge.EVENT_BUS.register(new DespawningEggsHatchEggEvent());
		}
			if ((ConfigHandler.GENERAL.enableDismountEntity.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Dismount Entity")) {
			MinecraftForge.EVENT_BUS.register(new DismountEntityDismountEvent());
		}
			if ((ConfigHandler.GENERAL.enableDoubleDoors.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Double Doors")) {
			MinecraftForge.EVENT_BUS.register(new DoubleDoorsDoorEvent());
		}
			if ((ConfigHandler.GENERAL.enableDragonDropsElytra.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Dragon Drops Elytra")) {
			MinecraftForge.EVENT_BUS.register(new DragonDropsElytraDragonEvent());
		}
			if ((ConfigHandler.GENERAL.enableEasyElytraTakeoff.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Easy Elytra Takeoff")) {
			MinecraftForge.EVENT_BUS.register(new EasyElytraTakeoffElytraEvent());
		}
			if ((ConfigHandler.GENERAL.enableEdibles.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Edibles")) {
			MinecraftForge.EVENT_BUS.register(new EdiblesEdibleEvent());
		}
			if ((ConfigHandler.GENERAL.enableEndPortalRecipe.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("End Portal Recipe")) {
			MinecraftForge.EVENT_BUS.register(new EndPortalRecipeEndPortalEvent());
		}
			if ((ConfigHandler.GENERAL.enableErodingStoneEntities.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Eroding Stone Entities")) {
			if (ErodingStoneEntitiesUtil.populateArrays()) {
			MinecraftForge.EVENT_BUS.register(new ErodingStoneEntitiesEntityEvent());
		}
		}
			if ((ConfigHandler.GENERAL.enableExtendedBoneMeal.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Extended Bone Meal")) {
			MinecraftForge.EVENT_BUS.register(new ExtendedBoneMealExtendedEvent());
		}
			if ((ConfigHandler.GENERAL.enableExtractPoison.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Extract Poison")) {
			MinecraftForge.EVENT_BUS.register(new ExtractPoisonEntityEvent());
		}
			if ((ConfigHandler.GENERAL.enableFallThroughSlime.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Fall Through Slime")) {
			MinecraftForge.EVENT_BUS.register(new FallThroughSlimeSlimeEvent());
		}
			if ((ConfigHandler.GENERAL.enableFireSpreadTweaks.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Fire Spread Tweaks")) {
			MinecraftForge.EVENT_BUS.register(new FireSpreadTweaksFireSpreadEvent());
		}
			if ((ConfigHandler.GENERAL.enableFirstJoinMessage.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("First Join Message")) {
			MinecraftForge.EVENT_BUS.register(new FirstJoinMessageFirstSpawnEvent());
		}
			if ((ConfigHandler.GENERAL.enableFishOnTheLine.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Fish On The Line")) {
			MinecraftForge.EVENT_BUS.register(new FishOnTheLineFishOnTheLineEvent());
		}
			if ((ConfigHandler.GENERAL.enableFixedAnvilRepairCost.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Fixed Anvil Repair Cost")) {
			MinecraftForge.EVENT_BUS.register(new FixedAnvilRepairCostRepairEvent());
		}
			if ((ConfigHandler.GENERAL.enableGiantSpawn.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Giant Spawn")) {
			MinecraftForge.EVENT_BUS.register(new GiantSpawnGiantEvent());
			new SAMObject(EntityType.ZOMBIE, EntityType.GIANT, null, GiantSpawnConfigHandler.GENERAL.chanceSurfaceZombieIsGiant.get(), false, false, GiantSpawnConfigHandler.GENERAL.onlySpawnGiantOnSurface.get());
		}
			if ((ConfigHandler.GENERAL.enableGrassSeeds.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Grass Seeds")) {
			MinecraftForge.EVENT_BUS.register(new GrassSeedsGrassEvent());
		}
			if ((ConfigHandler.GENERAL.enableGrindstoneSharperTools.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Grindstone Sharper Tools")) {
			MinecraftForge.EVENT_BUS.register(new GrindstoneSharperToolsGrindEvent());
		}
			if ((ConfigHandler.GENERAL.enableGUIClock.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("GUI Clock") && FMLEnvironment.dist.equals(Dist.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new GUIClockGUIEvent(Minecraft.getInstance()));
		}
			if ((ConfigHandler.GENERAL.enableGUICompass.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("GUI Compass") && FMLEnvironment.dist.equals(Dist.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new GUICompassGUIEvent(Minecraft.getInstance()));
		}
			if ((ConfigHandler.GENERAL.enableGUIFollowers.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("GUI Followers") && FMLEnvironment.dist.equals(Dist.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new GUIFollowersGUIEvent(Minecraft.getInstance()));
			MinecraftForge.EVENT_BUS.register(new GUIFollowersFollowerEvent());
		}
			if ((ConfigHandler.GENERAL.enableHandOverYourItems.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Hand Over Your Items")) {
			MinecraftForge.EVENT_BUS.register(new HandOverYourItemsHandOverEvent());
		}
			if ((ConfigHandler.GENERAL.enableHealingCampfire.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Healing Campfire")) {
			MinecraftForge.EVENT_BUS.register(new HealingCampfireCampfireEvent());
		}
			if ((ConfigHandler.GENERAL.enableHideHands.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Hide Hands") && FMLEnvironment.dist.equals(Dist.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new HideHandsHandEvent());
		}
			if ((ConfigHandler.GENERAL.enableHuskSpawn.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Husk Spawn")) {
			new SAMObject(EntityType.ZOMBIE, EntityType.HUSK, null, HuskSpawnConfigHandler.GENERAL.chanceZombieIsHusk.get(), false, false, false);
		}
			if ((ConfigHandler.GENERAL.enableIcePreventsCropGrowth.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Ice Prevents Crop Growth")) {
			MinecraftForge.EVENT_BUS.register(new IcePreventsCropGrowthCropEvent());
		}
			if ((ConfigHandler.GENERAL.enableInfiniteTrading.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Infinite Trading")) {
			MinecraftForge.EVENT_BUS.register(new InfiniteTradingVillagerEvent());
		}
			if ((ConfigHandler.GENERAL.enableInventoryTotem.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Inventory Totem")) {
			MinecraftForge.EVENT_BUS.register(new InventoryTotemTotemEvent());
		}
			if ((ConfigHandler.GENERAL.enableJustMobHeads.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Just Mob Heads")) {
			HeadData.init();
			MinecraftForge.EVENT_BUS.register(new JustMobHeadsHeadDropEvent());
		}
			if ((ConfigHandler.GENERAL.enableJustPlayerHeads.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Just Player Heads")) {
			MinecraftForge.EVENT_BUS.register(new JustPlayerHeadsPlayerEvent());
		}
			if ((ConfigHandler.GENERAL.enableKelpFertilizer.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Kelp Fertilizer")) {
			MinecraftForge.EVENT_BUS.register(new KelpFertilizerKelpEvent());
			RecipeManager.initDispenserBehavior();
		}
			if ((ConfigHandler.GENERAL.enableMilkAllTheMobs.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Milk All The Mobs")) {
			MinecraftForge.EVENT_BUS.register(new MilkAllTheMobsMilkEvent());
		}
			if ((ConfigHandler.GENERAL.enableMineralChance.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Mineral Chance")) {
			MinecraftForge.EVENT_BUS.register(new MineralChanceMiningEvent());
		}
			if ((ConfigHandler.GENERAL.enableMooshroomSpawn.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Mooshroom Spawn")) {
			new SAMObject(EntityType.COW, EntityType.MOOSHROOM, null, MooshroomSpawnConfigHandler.GENERAL.chanceCowIsMooshroom.get(), false, false, false);
		}
			if ((ConfigHandler.GENERAL.enableMooshroomTweaks.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Mooshroom Tweaks")) {
			MinecraftForge.EVENT_BUS.register(new MooshroomTweaksMooshroomEvent());
		}
			if ((ConfigHandler.GENERAL.enableMoreZombieVillagers.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("More Zombie Villagers")) {
			new SAMObject(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, null, MoreZombieVillagersConfigHandler.GENERAL.zombieIsVillagerChance.get(), false, false, false);
		}
			if ((ConfigHandler.GENERAL.enableMoveBoats.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Move Boats")) {
			MinecraftForge.EVENT_BUS.register(new MoveBoatsBoatEvent());
		}
			if ((ConfigHandler.GENERAL.enableMoveMinecarts.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Move Minecarts")) {
			MinecraftForge.EVENT_BUS.register(new MoveMinecartsMinecartEvent());
		}
			if ((ConfigHandler.GENERAL.enableNameTagTweaks.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Name Tag Tweaks")) {
			MinecraftForge.EVENT_BUS.register(new NameTagTweaksNameTagEvent());
		}
			if ((ConfigHandler.GENERAL.enableNaturallyChargedCreepers.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Naturally Charged Creepers")) {
			MinecraftForge.EVENT_BUS.register(new NaturallyChargedCreepersEntityEvent());
		}
			if ((ConfigHandler.GENERAL.enableNetherPortalSpread.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Nether Portal Spread")) {
			try {
			NetherPortalSpreadUtil.loadSpreadBlocks();
			} catch (IOException ex) {
			System.out.println(e);
		}
			MinecraftForge.EVENT_BUS.register(new NetherPortalSpreadSpreadEvent());
		}
			if ((ConfigHandler.GENERAL.enableNoHostilesAroundCampfire.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("No Hostiles Around Campfire")) {
			MinecraftForge.EVENT_BUS.register(new NoHostilesAroundCampfireCampfireEvent());
		}
			if ((ConfigHandler.GENERAL.enableNutritiousMilk.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Nutritious Milk")) {
			MinecraftForge.EVENT_BUS.register(new NutritiousMilkMilkEvent());
		}
			if ((ConfigHandler.GENERAL.enableOmegaMute.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Omega Mute") && FMLEnvironment.dist.equals(Dist.CLIENT)) {
			try {
			OmegaMuteUtil.loadSoundFile();
			} catch (Exception ex) {
			System.out.println("Something went wrong while generating the sound file. Omega Mute has been disabled.");
		}
			MinecraftForge.EVENT_BUS.register(new OmegaMuteMuteEvent());
		}
			if ((ConfigHandler.GENERAL.enablePassiveEndermen.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Passive Endermen")) {
			MinecraftForge.EVENT_BUS.register(new PassiveEndermenEndermenEvent());
		}
			if ((ConfigHandler.GENERAL.enablePassiveShield.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Passive Shield")) {
			if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new PassiveShieldClientEvent());
		}
			MinecraftForge.EVENT_BUS.register(new PassiveShieldServerEvent());
		}
			if ((ConfigHandler.GENERAL.enablePetNames.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Pet Names")) {
			MinecraftForge.EVENT_BUS.register(new PetNamesEntityEvent());
		}
			if ((ConfigHandler.GENERAL.enableQuickPaths.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Quick Paths")) {
			MinecraftForge.EVENT_BUS.register(new QuickPathsPathEvent());
		}
			if ((ConfigHandler.GENERAL.enableRainBeGoneRitual.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Rain Be Gone Ritual")) {
			MinecraftForge.EVENT_BUS.register(new RainBeGoneRitualRitualEvent());
		}
			if ((ConfigHandler.GENERAL.enableRandomBoneMealFlowers.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Random Bone Meal Flowers")) {
			try {
			RandomBoneMealFlowersUtil.setFlowerList();
			} catch (Exception ex) {
			System.out.println("!!! Something went wrong while initializing Random Bone Meal Flowers. The mod has been disabled.");
		}
			MinecraftForge.EVENT_BUS.register(new RandomBoneMealFlowersFlowerEvent());
		}
			if ((ConfigHandler.GENERAL.enableRandomSheepColours.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Random Sheep Colours")) {
			RandomSheepColoursUtil.initColours();
			MinecraftForge.EVENT_BUS.register(new RandomSheepColoursSheepEvent());
		}
			if ((ConfigHandler.GENERAL.enableRandomShulkerColours.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Random Shulker Colours")) {
			RandomShulkerColoursUtil.initColours();
			MinecraftForge.EVENT_BUS.register(new RandomShulkerColoursShulkerEvent());
		}
			if ((ConfigHandler.GENERAL.enableRandomVillageNames.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Random Village Names")) {
			MinecraftForge.EVENT_BUS.register(new RandomVillageNamesSetVillageSignEvent());
		}
			if ((ConfigHandler.GENERAL.enableRealisticBees.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Realistic Bees")) {
			MinecraftForge.EVENT_BUS.register(new RealisticBeesBeeEvent());
		}
			if ((ConfigHandler.GENERAL.enableRecast.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Recast")) {
			MinecraftForge.EVENT_BUS.register(new RecastRecastEvent());
		}
			if ((ConfigHandler.GENERAL.enableReplantingCrops.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Replanting Crops")) {
			MinecraftForge.EVENT_BUS.register(new ReplantingCropsCropEvent());
		}
			if ((ConfigHandler.GENERAL.enableRespawningShulkers.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Respawning Shulkers")) {
			MinecraftForge.EVENT_BUS.register(new RespawningShulkersShulkerEvent());
		}
			if ((ConfigHandler.GENERAL.enableScaffoldingDropsNearby.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Scaffolding Drops Nearby")) {
			MinecraftForge.EVENT_BUS.register(new ScaffoldingDropsNearbyScaffoldingEvent());
		}
			if ((ConfigHandler.GENERAL.enableShulkerDropsTwo.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Shulker Drops Two")) {
			MinecraftForge.EVENT_BUS.register(new ShulkerDropsTwoEntityEvent());
		}
			if ((ConfigHandler.GENERAL.enableSkeletonHorseSpawn.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Skeleton Horse Spawn")) {
			MinecraftForge.EVENT_BUS.register(new SkeletonHorseSpawnSkeletonHorseEvent());
			new SAMObject(EntityType.SKELETON, EntityType.SKELETON_HORSE, null, SkeletonHorseSpawnConfigHandler.GENERAL.chanceSurfaceSkeletonHasHorse.get(), false, true, SkeletonHorseSpawnConfigHandler.GENERAL.onlySpawnSkeletonHorsesOnSurface.get());
		}
			if ((ConfigHandler.GENERAL.enableSleepSooner.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Sleep Sooner")) {
			MinecraftForge.EVENT_BUS.register(new SleepSoonerPlayerEvent());
		}
			if ((ConfigHandler.GENERAL.enableSmallerNetherPortals.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Smaller Nether Portals")) {
			MinecraftForge.EVENT_BUS.register(new SmallerNetherPortalsPortalEvent());
		}
			if ((ConfigHandler.GENERAL.enableSnowballsFreezeMobs.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Snowballs Freeze Mobs")) {
			MinecraftForge.EVENT_BUS.register(new SnowballsFreezeMobsSnowEvent());
		}
			if ((ConfigHandler.GENERAL.enableSofterHayBales.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Softer Hay Bales")) {
			MinecraftForge.EVENT_BUS.register(new SofterHayBalesFallEvent());
		}
			if ((ConfigHandler.GENERAL.enableSpidersProduceWebs.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Spiders Produce Webs")) {
			MinecraftForge.EVENT_BUS.register(new SpidersProduceWebsSpiderEvent());
		}
			if ((ConfigHandler.GENERAL.enableStackRefill.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Stack Refill")) {
			MinecraftForge.EVENT_BUS.register(new StackRefillRefillEvent());
		}
			if ((ConfigHandler.GENERAL.enableStarterKit.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Starter Kit")) {
			try {
			StarterKitUtil.getOrCreateGearConfig(true);
			} catch (Exception ex) { }
			MinecraftForge.EVENT_BUS.register(new StarterKitFirstSpawnEvent());
		}
			if ((ConfigHandler.GENERAL.enableStraySpawn.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Stray Spawn")) {
			new SAMObject(EntityType.SKELETON, EntityType.STRAY, Items.BOW, StraySpawnConfigHandler.GENERAL.chanceSkeletonIsStray.get(), false, false, false);
		}
			if ((ConfigHandler.GENERAL.enableSuperflatWorldNoSlimes.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Superflat World No Slimes")) {
			MinecraftForge.EVENT_BUS.register(new SuperflatWorldNoSlimesSlimeEvent());
		}
			if ((ConfigHandler.GENERAL.enableTNTBreaksBedrock.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("TNT Breaks Bedrock")) {
			MinecraftForge.EVENT_BUS.register(new TNTBreaksBedrockBoomEvent());
		}
			if ((ConfigHandler.GENERAL.enableTranscendingTrident.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Transcending Trident")) {
			MinecraftForge.EVENT_BUS.register(new TranscendingTridentTridentEvent());
		}
			if ((ConfigHandler.GENERAL.enableTreeHarvester.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Tree Harvester")) {
			MinecraftForge.EVENT_BUS.register(new TreeHarvesterTreeEvent());
			if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new TreeHarvesterSoundHarvestEvent());
		}
		}
			if ((ConfigHandler.GENERAL.enableUnderwaterEnchanting.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Underwater Enchanting")) {
			MinecraftForge.EVENT_BUS.register(new UnderwaterEnchantingEnhantmentEvent());
		}
			if ((ConfigHandler.GENERAL.enableVillageSpawnPoint.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Village Spawn Point")) {
			MinecraftForge.EVENT_BUS.register(new VillageSpawnPointVillageSpawnEvent());
		}
			if ((ConfigHandler.GENERAL.enableVillagerDeathMessages.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Villager Death Messages")) {
			MinecraftForge.EVENT_BUS.register(new VillagerDeathMessagesVillagerEvent());
		}
			if ((ConfigHandler.GENERAL.enableVillagerNames.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Villager Names")) {
			try {
			Names.setCustomNames();
			} catch (IOException ex) { }
			MinecraftForge.EVENT_BUS.register(new VillagerNamesVillagerEvent());
		}
			if ((ConfigHandler.GENERAL.enableWeakerSpiderwebs.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Weaker Spiderwebs")) {
			MinecraftForge.EVENT_BUS.register(new WeakerSpiderwebsWebEvent());
		}
			if ((ConfigHandler.GENERAL.enableWoolTweaks.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Wool Tweaks")) {
			WoolTweaksUtil.initiateColourMaps();
			MinecraftForge.EVENT_BUS.register(new WoolTweaksWoolClickEvent());
		}
			if ((ConfigHandler.GENERAL.enableZombieHorseSpawn.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Zombie Horse Spawn")) {
			MinecraftForge.EVENT_BUS.register(new ZombieHorseSpawnZombieHorseEvent());
			new SAMObject(EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, null, ZombieHorseSpawnConfigHandler.GENERAL.chanceSurfaceZombieHasHorse.get(), false, true, ZombieHorseSpawnConfigHandler.GENERAL.onlySpawnZombieHorsesOnSurface.get());
		}
			if ((ConfigHandler.GENERAL.enableZombieProofDoors.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Zombie Proof Doors")) {
			MinecraftForge.EVENT_BUS.register(new ZombieProofDoorsZombieJoinEvent());
		}
			if ((ConfigHandler.GENERAL.enableZombieVillagersFromSpawner.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Zombie Villagers From Spawner")) {
			new SAMObject(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, null, ZombieVillagersFromSpawnerConfigHandler.GENERAL.isZombieVillagerChance.get(), true, false, false);
		}
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> e) {
			if ((ConfigHandler.GENERAL.enableBiggerSpongeAbsorptionRadius.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Bigger Sponge Absorption Radius")) {
			e.getRegistry().registerAll(
				BiggerSpongeAbsorptionRadiusVariables.spongeblock = new ExtendedSpongeBlock(BlockBehaviour.Properties.of(Material.SPONGE).strength(0.6F).sound(SoundType.GRASS)).setRegistryName(Blocks.SPONGE.getRegistryName())
			);
		}
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> e) {
			if ((ConfigHandler.GENERAL.enableBiggerSpongeAbsorptionRadius.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Bigger Sponge Absorption Radius")) {
			e.getRegistry().registerAll(
				new BlockItem(BiggerSpongeAbsorptionRadiusVariables.spongeblock, new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)).setRegistryName(Items.SPONGE.getRegistryName())
			);
		}
			if ((ConfigHandler.GENERAL.enableExtendedCreativeInventory.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Extended Creative Inventory")) {
			if (ExtendedCreativeInventoryVariables.EXTENDED == null) {
			List<String> rawconfig = ConfigFunctions.getRawConfigValues(com.natamus.thevanillaexperience.mods.extendedcreativeinventory.util.Reference.MOD_ID, true);
			int index = 4;
			if (rawconfig.size() > 0) {
				try {
						String stringint = String.join(" ", rawconfig).replaceAll("[^0-9]", "");
						int newindex = Integer.parseInt(stringint);
						index = newindex;
						} catch (Exception ex) { }
			}
			ExtendedCreativeInventoryVariables.EXTENDED = new ExtendedItemGroup(index, "extended");
		}
			IForgeRegistry<Item> registry = e.getRegistry();
			for (ResourceLocation rl : ForgeRegistries.ITEMS.getKeys()) {
			Item item = ForgeRegistries.ITEMS.getValue(rl);
			if (item.equals(Items.AIR)) {
				continue;
			}
			CreativeModeTab itemgroup = item.getItemCategory();
			if (itemgroup == null) {
				try {
						ExtendedCreativeInventoryVariables.item_group.set(item, ExtendedCreativeInventoryVariables.EXTENDED);
						} catch (Exception ex) { }
				registry.registerAll(item);
			}
		}	
		}
			if ((ConfigHandler.GENERAL.enableTranscendingTrident.get() || ConfigHandler.GENERAL._enableALL.get()) && TveUtil.shouldLoadMod("Transcending Trident")) {
			e.getRegistry().registerAll(
				(new ExtendedTridentItem((new Item.Properties()).durability(250).tab(CreativeModeTab.TAB_COMBAT)).setRegistryName(Items.TRIDENT.getRegistryName()))
			);
		}
	}
}