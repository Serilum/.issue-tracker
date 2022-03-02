/*
 * This is the latest source code of Difficulty Lock.
 * Minecraft version: 1.19.x, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Difficulty Lock ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.difficultylock.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.difficultylock.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> forcePeaceful = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> forceEasy = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> forceNormal = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> forceHard = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> shouldLockDifficulty = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> shouldChangeDifficultyWhenAlreadyLocked = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("forcePeaceful", ConfigTypes.BOOLEAN, false)
			.withComment("Priority 1: Sets the difficulty in any world to peaceful when enabled.")
			.finishValue(forcePeaceful::mirror)

			.beginValue("forceEasy", ConfigTypes.BOOLEAN, false)
			.withComment("Priority 2: Sets the difficulty in any world to easy when enabled.")
			.finishValue(forceEasy::mirror)

			.beginValue("forceNormal", ConfigTypes.BOOLEAN, false)
			.withComment("Priority 3: Sets the difficulty in any world to normal when enabled.")
			.finishValue(forceNormal::mirror)

			.beginValue("forceHard", ConfigTypes.BOOLEAN, true)
			.withComment("Priority 4: Sets the difficulty in any world to hard when enabled.")
			.finishValue(forceHard::mirror)

			.beginValue("shouldLockDifficulty", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, locks the difficulty in any world so it cannot be changed.")
			.finishValue(shouldLockDifficulty::mirror)

			.beginValue("shouldChangeDifficultyWhenAlreadyLocked", ConfigTypes.BOOLEAN, false)
			.withComment("When enabled, also sets the difficulty in worlds where it has already been locked.")
			.finishValue(shouldChangeDifficultyWhenAlreadyLocked::mirror)

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