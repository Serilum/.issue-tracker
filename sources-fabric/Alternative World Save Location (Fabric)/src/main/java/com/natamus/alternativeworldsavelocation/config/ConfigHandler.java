/*
 * This is the latest source code of Alternative World Save Location.
 * Minecraft version: 1.19.2, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.alternativeworldsavelocation.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.alternativeworldsavelocation.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> changeDefaultWorldSaveLocation = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> defaultMinecraftWorldSaveLocation = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> changeDefaultWorldBackupLocation = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> defaultMinecraftWorldBackupLocation = PropertyMirror.create(ConfigTypes.STRING);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("changeDefaultWorldSaveLocation", ConfigTypes.BOOLEAN, false)
			.withComment("Disabled by default, to prevent unwanted modpack behaviour. When enabled, changes the world location to 'defaultMinecraftWorldSaveLocation'")
			.finishValue(changeDefaultWorldSaveLocation::mirror)

			.beginValue("defaultMinecraftWorldSaveLocation", ConfigTypes.STRING, System.getProperty("user.dir") + File.separator + "saves")
			.withComment("Use either \\\\ or / as a path separator. The location of the folder containing the world saves.")
			.finishValue(defaultMinecraftWorldSaveLocation::mirror)

			.beginValue("changeDefaultWorldBackupLocation", ConfigTypes.BOOLEAN, false)
			.withComment("Disabled by default. Enable this to set a specific world backup folder. If disabled, this will be set to 'defaultMinecraftWorldSaveLocation'/_Backup.")
			.finishValue(changeDefaultWorldBackupLocation::mirror)

			.beginValue("defaultMinecraftWorldBackupLocation", ConfigTypes.STRING, System.getProperty("user.dir") + File.separator + "backups")
			.withComment("Use either \\\\ or / as a path separator. The world backup folder if both 'changeDefaultWorldSaveLocation' and 'changeDefaultWorldBackupLocation' are enabled.")
			.finishValue(defaultMinecraftWorldBackupLocation::mirror)

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