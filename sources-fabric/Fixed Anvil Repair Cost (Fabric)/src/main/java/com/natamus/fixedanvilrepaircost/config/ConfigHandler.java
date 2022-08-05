/*
 * This is the latest source code of Fixed Anvil Repair Cost.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.fixedanvilrepaircost.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.fixedanvilrepaircost.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Integer> repairCostLevelAmount = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> repairCostMaterialAmount = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Double> percentRepairedPerAction = PropertyMirror.create(ConfigTypes.DOUBLE);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("repairCostLevelAmount", ConfigTypes.INTEGER, 3)
			.withComment("The amount of level it costs to repair an item on an anvil. A value of 0 keeps vanilla behaviour.")
			.finishValue(repairCostLevelAmount::mirror)

			.beginValue("repairCostMaterialAmount", ConfigTypes.INTEGER, 1)
			.withComment("The amount of material it costs to repair an item on an anvil. A value of 0 keeps vanilla behaviour.")
			.finishValue(repairCostMaterialAmount::mirror)

			.beginValue("percentRepairedPerAction", ConfigTypes.DOUBLE, 0.3333)
			.withComment("How much the item is repaired per action. By default 33.33%, so 3 of 'repairCostMaterialAmount' fully repairs any item.")
			.finishValue(percentRepairedPerAction::mirror)

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