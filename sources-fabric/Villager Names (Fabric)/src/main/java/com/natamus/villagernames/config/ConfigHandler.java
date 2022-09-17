/*
 * This is the latest source code of Villager Names.
 * Minecraft version: 1.19.2, mod version: 4.1.
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

package com.natamus.villagernames.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.villagernames.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> _useCustomNames = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> _useFemaleNames = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> _useMaleNames = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> nameModdedVillagers = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("_useCustomNames", ConfigTypes.BOOLEAN, true)
			.withComment("Use the custom name list, editable in ./mods/villagernames/customnames.txt, seperated by a comma.")
			.finishValue(_useCustomNames::mirror)

			.beginValue("_useFemaleNames", ConfigTypes.BOOLEAN, true)
			.withComment("Use the list of pre-defined female names when naming villagers.")
			.finishValue(_useFemaleNames::mirror)

			.beginValue("_useMaleNames", ConfigTypes.BOOLEAN, true)
			.withComment("Use the list of pre-defined male names when naming villagers.")
			.finishValue(_useMaleNames::mirror)

			.beginValue("nameModdedVillagers", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, also gives modded villagers a name. If you've found a 'villager'-entity that isn't named let me know by opening an issue so I can add it in.")
			.finishValue(nameModdedVillagers::mirror)

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