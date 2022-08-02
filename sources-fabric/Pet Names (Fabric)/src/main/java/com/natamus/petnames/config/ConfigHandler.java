/*
 * This is the latest source code of Pet Names.
 * Minecraft version: 1.19.1, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.petnames.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.petnames.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> _useFemaleNames = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> _useMaleNames = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> nameWolves = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> nameCats = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> nameHorses = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> nameDonkeys = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> nameMules = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> nameLlamas = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("_useFemaleNames", ConfigTypes.BOOLEAN, true)
			.withComment("Use the list of female names when naming pets.")
			.finishValue(_useFemaleNames::mirror)

			.beginValue("_useMaleNames", ConfigTypes.BOOLEAN, true)
			.withComment("Use the list of male names when naming pets.")
			.finishValue(_useMaleNames::mirror)

			.beginValue("nameWolves", ConfigTypes.BOOLEAN, true)
			.withComment("Give baby wolves a name.")
			.finishValue(nameWolves::mirror)

			.beginValue("nameCats", ConfigTypes.BOOLEAN, true)
			.withComment("Give kittens a name.")
			.finishValue(nameCats::mirror)

			.beginValue("nameHorses", ConfigTypes.BOOLEAN, true)
			.withComment("Give baby horses a name.")
			.finishValue(nameHorses::mirror)

			.beginValue("nameDonkeys", ConfigTypes.BOOLEAN, true)
			.withComment("Give baby donkeys a name.")
			.finishValue(nameDonkeys::mirror)

			.beginValue("nameMules", ConfigTypes.BOOLEAN, true)
			.withComment("Give baby mules a name.")
			.finishValue(nameMules::mirror)

			.beginValue("nameLlamas", ConfigTypes.BOOLEAN, true)
			.withComment("Give baby llamas a name.")
			.finishValue(nameLlamas::mirror)

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