/*
 * This is the latest source code of Set World Spawn Point.
 * Minecraft version: 1.19.2, mod version: 2.5.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.setworldspawnpoint.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.setworldspawnpoint.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> _forceExactSpawn = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> xCoordSpawnPoint = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> yCoordSpawnPoint = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> zCoordSpawnPoint = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("_forceExactSpawn", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, spawns players on the exact world spawn instead of around it.")
			.finishValue(_forceExactSpawn::mirror)

			.beginValue("xCoordSpawnPoint", ConfigTypes.INTEGER, 0)
			.withComment("The X coordinate of the spawn point of newly created worlds.")
			.finishValue(xCoordSpawnPoint::mirror)

			.beginValue("yCoordSpawnPoint", ConfigTypes.INTEGER, -1)
			.withComment("The Y coordinate of the spawn point of newly created worlds. By default -1, which means it'll be the first solid block descending from y=256.")
			.finishValue(yCoordSpawnPoint::mirror)

			.beginValue("zCoordSpawnPoint", ConfigTypes.INTEGER, 0)
			.withComment("The Z coordinate of the spawn point of newly created worlds.")
			.finishValue(zCoordSpawnPoint::mirror)

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