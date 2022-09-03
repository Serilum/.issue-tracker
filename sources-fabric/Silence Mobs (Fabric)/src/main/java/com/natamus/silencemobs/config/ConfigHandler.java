/*
 * This is the latest source code of Silence Mobs.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.silencemobs.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.silencemobs.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> onlyAllowCommandWhenCheatsEnabled = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> mustHoldStick = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> renameSilencedMobs = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("onlyAllowCommandWhenCheatsEnabled", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, only allows the /silencestick command with cheats enabled.")
			.finishValue(onlyAllowCommandWhenCheatsEnabled::mirror)

			.beginValue("mustHoldStick", ConfigTypes.BOOLEAN, true)
			.withComment("If disabled, a stick will be generated via the /silencestick command instead of having to hold a stick while using the command.")
			.finishValue(mustHoldStick::mirror)

			.beginValue("renameSilencedMobs", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, whenever a player hits a non-silenced mob with The Silence Stick it will set their name to 'Silenced Entity'.")
			.finishValue(renameSilencedMobs::mirror)

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