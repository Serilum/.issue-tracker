/*
 * This is the latest source code of Bottled Air.
 * Minecraft version: 1.19.2, mod version: 1.6.
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

package com.natamus.bottledair.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.bottledair.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler {
	public static PropertyMirror<Boolean> disableWaterConsumptionUnderwater = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> holdFireTypeItemInOffhandToPreventWaterBottleCreation = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Double> chanceGlassBottleBreaksWithFireTypeInOffhand = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Integer> amountOfAirInBottles = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder()
			.beginValue("disableWaterConsumptionUnderwater", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, players will be unable to drink water bottles underwater to prevent getting unlimited oxygen.")
			.finishValue(disableWaterConsumptionUnderwater::mirror)

			.beginValue("holdFireTypeItemInOffhandToPreventWaterBottleCreation", ConfigTypes.BOOLEAN, true)
			.withComment("Whether the creation of water bottles should be prevented (evaporated) when holding a fire type block in the offhand.")
			.finishValue(holdFireTypeItemInOffhandToPreventWaterBottleCreation::mirror)

			.beginValue("chanceGlassBottleBreaksWithFireTypeInOffhand", ConfigTypes.DOUBLE, 0.5)
			.withComment("The chance a glass bottle breaks when the item in the offhand evaporates the water, giving back an empty (air) bottle.")
			.finishValue(chanceGlassBottleBreaksWithFireTypeInOffhand::mirror)

			.beginValue("amountOfAirInBottles", ConfigTypes.INTEGER, 150)
			.withComment("The amount of air an empty bottle contains. In vanilla Minecraft, 300 is the maximum air supply.")
			.finishValue(amountOfAirInBottles::mirror)

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