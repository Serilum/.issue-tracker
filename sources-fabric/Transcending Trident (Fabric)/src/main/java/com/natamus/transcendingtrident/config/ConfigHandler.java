/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.19.1, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.transcendingtrident.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.transcendingtrident.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> mustHoldBucketOfWater = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> tridentUseDuration = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Double> tridentUsePowerModifier = PropertyMirror.create(ConfigTypes.DOUBLE);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("mustHoldBucketOfWater", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, Riptide can only be used without rain when the user is holding a bucket of water.")
			.finishValue(mustHoldBucketOfWater::mirror)

			.beginValue("tridentUseDuration", ConfigTypes.INTEGER, 5)
			.withComment("The amount of time a player needs to charge the trident before being able to use Riptide. Minecraft's default is 10.")
			.finishValue(tridentUseDuration::mirror)

			.beginValue("tridentUsePowerModifier", ConfigTypes.DOUBLE, 3.0)
			.withComment("The riptide power of the trident is multiplied by this number on use. Allows traveling a greater distance with a single charge.")
			.finishValue(tridentUsePowerModifier::mirror)

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