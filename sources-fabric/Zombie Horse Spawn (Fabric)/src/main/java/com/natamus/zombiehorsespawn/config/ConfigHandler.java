/*
 * This is the latest source code of Zombie Horse Spawn.
 * Minecraft version: 1.19.2, mod version: 3.5.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.zombiehorsespawn.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.zombiehorsespawn.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Double> chanceSurfaceZombieHasHorse = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Boolean> shouldBurnZombieHorsesInDaylight = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> onlySpawnZombieHorsesOnSurface = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("chanceSurfaceZombieHasHorse", ConfigTypes.DOUBLE, 0.05)
			.withComment("The chance a zombie that has spawned on the surface is riding a horse.")
			.finishValue(chanceSurfaceZombieHasHorse::mirror)

			.beginValue("shouldBurnZombieHorsesInDaylight", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, burns zombie horses when daylight shines upon them.")
			.finishValue(shouldBurnZombieHorsesInDaylight::mirror)

			.beginValue("onlySpawnZombieHorsesOnSurface", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, a zombie horse with rider will only spawn on the surface.")
			.finishValue(onlySpawnZombieHorsesOnSurface::mirror)

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