/*
 * This is the latest source code of Bottle Your Xp.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.bottleyourxp.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.bottleyourxp.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> damageOnCreation = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> halfHeartDamageAmount = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> rawXpConsumedOnCreation = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("damageOnCreation", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, damages the user whenever they create an experience bottle.")
			.finishValue(damageOnCreation::mirror)

			.beginValue("halfHeartDamageAmount", ConfigTypes.INTEGER, 1)
			.withComment("The amount of damage the user takes times half a heart when creating an experience bottle.")
			.finishValue(halfHeartDamageAmount::mirror)

			.beginValue("rawXpConsumedOnCreation", ConfigTypes.INTEGER, 10)
			.withComment("The amount of raw xp it takes to produce an experience bottle. Be careful when setting this too low, as it may enable xp duplication.")
			.finishValue(rawXpConsumedOnCreation::mirror)

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