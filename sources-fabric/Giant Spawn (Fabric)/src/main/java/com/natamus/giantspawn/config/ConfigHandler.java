/*
 * This is the latest source code of Giant Spawn.
 * Minecraft version: 1.19.2, mod version: 3.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.giantspawn.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.giantspawn.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Double> chanceSurfaceZombieIsGiant = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Boolean> shouldBurnGiantsInDaylight = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> onlySpawnGiantOnSurface = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Double> giantMovementSpeedModifier = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Double> giantAttackDamageModifier = PropertyMirror.create(ConfigTypes.DOUBLE);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("chanceSurfaceZombieIsGiant", ConfigTypes.DOUBLE, 0.05)
			.withComment("The chance a zombie that has spawned on the surface is a giant.")
			.finishValue(chanceSurfaceZombieIsGiant::mirror)

			.beginValue("shouldBurnGiantsInDaylight", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, burns giants when daylight shines upon them.")
			.finishValue(shouldBurnGiantsInDaylight::mirror)

			.beginValue("onlySpawnGiantOnSurface", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, a giant will only spawn on the surface.")
			.finishValue(onlySpawnGiantOnSurface::mirror)

			.beginValue("giantMovementSpeedModifier", ConfigTypes.DOUBLE, 1.0)
			.withComment("The giant movement speed modifier relative to the base zombie movement speed.")
			.finishValue(giantMovementSpeedModifier::mirror)

			.beginValue("giantAttackDamageModifier", ConfigTypes.DOUBLE, 2.0)
			.withComment("The giant attack damage modifier relative to the base zombie attack damage.")
			.finishValue(giantAttackDamageModifier::mirror)

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