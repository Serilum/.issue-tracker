/*
 * This is the latest source code of Set World Spawn Point.
 * Minecraft version: 1.19.1, mod version: 2.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Set World Spawn Point ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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