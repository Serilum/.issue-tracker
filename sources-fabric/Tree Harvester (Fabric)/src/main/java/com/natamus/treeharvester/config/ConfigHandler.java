/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.19.2, mod version: 5.6.
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.treeharvester.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> mustHoldAxeForTreeHarvest = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> treeHarvestWithoutSneak = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> instantBreakLeavesAround = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> automaticallyFindBottomBlock = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableFastLeafDecay = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableNetherTrees = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableHugeMushrooms = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> replaceSaplingOnTreeHarvest = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> replaceMushroomOnMushroomHarvest = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> loseDurabilityPerHarvestedLog = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Double> loseDurabilityModifier = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Boolean> increaseExhaustionPerHarvestedLog = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Double> increaseExhaustionModifier = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Boolean> increaseHarvestingTimePerLog = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Double> increasedHarvestingTimePerLogModifier = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Integer> amountOfLeavesBrokenPerTick = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("mustHoldAxeForTreeHarvest", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, tree harvesting only works when a player is holding an axe in the main hand.")
			.finishValue(mustHoldAxeForTreeHarvest::mirror)

			.beginValue("treeHarvestWithoutSneak", ConfigTypes.BOOLEAN, false)
			.withComment("If enabled, tree harvesting works when not holding the sneak button. If disabled it's reversed, and only works when sneaking.")
			.finishValue(treeHarvestWithoutSneak::mirror)

			.beginValue("instantBreakLeavesAround", ConfigTypes.BOOLEAN, false)
			.withComment("If enabled, players instantly break the leaves as well as all logs of the tree when a bottom log is broken.")
			.finishValue(instantBreakLeavesAround::mirror)

			.beginValue("automaticallyFindBottomBlock", ConfigTypes.BOOLEAN, true)
			.withComment("Whether the mod should attempt to find the actual bottom log of the tree and start there. This means you can break a tree in the middle and it will still completely be felled.")
			.finishValue(automaticallyFindBottomBlock::mirror)

			.beginValue("enableFastLeafDecay", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, the leaves around a broken tree will quickly disappear. Only works with 'instantBreakLeavesAround' disabled.")
			.finishValue(enableFastLeafDecay::mirror)

			.beginValue("enableNetherTrees", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, the warped stem/crimson trees in the nether will also be chopped down quickly.")
			.finishValue(enableNetherTrees::mirror)

			.beginValue("enableHugeMushrooms", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, giant/huge mushrooms will also be chopped down quickly.")
			.finishValue(enableHugeMushrooms::mirror)

			.beginValue("replaceSaplingOnTreeHarvest", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, automatically replaces the sapling from the drops when a tree is harvested.")
			.finishValue(replaceSaplingOnTreeHarvest::mirror)

			.beginValue("replaceMushroomOnMushroomHarvest", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, automatically replaces the sapling from the drops when a huge mushroom is harvested and 'enableHugeMushrooms' is enabled.")
			.finishValue(replaceMushroomOnMushroomHarvest::mirror)

			.beginValue("loseDurabilityPerHarvestedLog", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, for every log harvested, the axe held loses durability.")
			.finishValue(loseDurabilityPerHarvestedLog::mirror)

			.beginValue("loseDurabilityModifier", ConfigTypes.DOUBLE, 1.0)
			.withComment("Here you can set how much durability chopping down a tree should take from the axe. For example if set to 0.1, this means that every 10 logs take 1 durability.")
			.finishValue(loseDurabilityModifier::mirror)

			.beginValue("increaseExhaustionPerHarvestedLog", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, players' exhaustion level increases 0.005 per harvested log (Minecraft's default per broken block) * increaseExhaustionModifier.")
			.finishValue(increaseExhaustionPerHarvestedLog::mirror)

			.beginValue("increaseExhaustionModifier", ConfigTypes.DOUBLE, 1.0)
			.withComment("This determines how much exhaustion should be added to the player per harvested log. By default 0.005 * 1.0.")
			.finishValue(increaseExhaustionModifier::mirror)

			.beginValue("increaseHarvestingTimePerLog", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, harvesting time will increase per existing log in the tree. The amount is determined by 'increasedHarvestingTimePerLogModifier'.")
			.finishValue(increaseHarvestingTimePerLog::mirror)

			.beginValue("increasedHarvestingTimePerLogModifier", ConfigTypes.DOUBLE, 0.1)
			.withComment("How much longer it takes to harvest a tree with 'increaseHarvestingTimePerLog' enabled. The actual speed is: newSpeed = originalSpeed / (1 + (logCount * increasedHarvestingTimePerLogModifier)).")
			.finishValue(increasedHarvestingTimePerLogModifier::mirror)

			.beginValue("amountOfLeavesBrokenPerTick", ConfigTypes.INTEGER, 3)
			.withComment("How many leaves should be broken per tick after a tree has been harvested. Increasing this will speed up the fast leaf decay, but costs more processing power per tick.")
			.finishValue(amountOfLeavesBrokenPerTick::mirror)

			.build();

	private static void writeDefaultConfig(Path path, JanksonValueSerializer serializer) {
		try (OutputStream s = new BufferedOutputStream(Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW))) {
			FiberSerialization.serialize(CONFIG, s, serializer);
		} catch (IOException ignored) {}

	}

	public static void setup() {
		JanksonValueSerializer serializer = new JanksonValueSerializer(false);
		Path p = Paths.get("config", Reference.MOD_ID + ".json");
		writeDefaultConfig(p, serializer);

		try (InputStream s = new BufferedInputStream(Files.newInputStream(p, StandardOpenOption.READ, StandardOpenOption.CREATE))) {
			FiberSerialization.deserialize(CONFIG, s, serializer);
		} catch (IOException | ValueDeserializationException e) {
			System.out.println("Error loading config");
		}
	}
}