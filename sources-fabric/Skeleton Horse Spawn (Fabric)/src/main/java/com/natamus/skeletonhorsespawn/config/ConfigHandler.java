/*
 * This is the latest source code of Skeleton Horse Spawn.
 * Minecraft version: 1.19.1, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.skeletonhorsespawn.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.skeletonhorsespawn.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Double> chanceSurfaceSkeletonHasHorse = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Boolean> shouldBurnSkeletonHorsesInDaylight = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> onlySpawnSkeletonHorsesOnSurface = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("chanceSurfaceSkeletonHasHorse", ConfigTypes.DOUBLE, 0.05)
			.withComment("The chance a skeleton that has spawned on the surface is riding a horse.")
			.finishValue(chanceSurfaceSkeletonHasHorse::mirror)

			.beginValue("shouldBurnSkeletonHorsesInDaylight", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, burns skeleton horses when daylight shines upon them.")
			.finishValue(shouldBurnSkeletonHorsesInDaylight::mirror)

			.beginValue("onlySpawnSkeletonHorsesOnSurface", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, a skeleton horse with rider will only spawn on the surface.")
			.finishValue(onlySpawnSkeletonHorsesOnSurface::mirror)

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