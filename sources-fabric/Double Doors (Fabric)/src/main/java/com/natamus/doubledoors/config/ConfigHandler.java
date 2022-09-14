/*
 * This is the latest source code of Double Doors.
 * Minecraft version: 1.19.2, mod version: 3.7.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.doubledoors.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.doubledoors.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler {
	public static PropertyMirror<Boolean> enableRecursiveOpening = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> recursiveOpeningMaxBlocksDistance = PropertyMirror.create(ConfigTypes.INTEGER);
	
	public static PropertyMirror<Boolean> enableDoors = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableFenceGates = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableTrapdoors = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder()
			.beginValue("enableRecursiveOpening", ConfigTypes.BOOLEAN, true)
			.withComment("Whether the recursive opening feature should be enabled. This allows you to for example build a giant door with trapdoors which will all open at the same time, as long as they are connected. The 'recursiveOpeningMaxBlocksDistance' config option determines how far the function should search.")
			.finishValue(enableRecursiveOpening::mirror)
			
			.beginValue("recursiveOpeningMaxBlocksDistance", ConfigTypes.INTEGER, 10)
			.withComment("How many blocks the recursive function should search when 'enableRecursiveOpening' is enabled.")
			.finishValue(recursiveOpeningMaxBlocksDistance::mirror)
			
			.beginValue("enableDoors", ConfigTypes.BOOLEAN, true)
			.withComment("When enables, the mod works with double doors.")
			.finishValue(enableDoors::mirror)

			.beginValue("enableFenceGates", ConfigTypes.BOOLEAN, true)
			.withComment("When enables, the mod works with double fence gates.")
			.finishValue(enableFenceGates::mirror)

			.beginValue("enableTrapdoors", ConfigTypes.BOOLEAN, true)
			.withComment("When enables, the mod works with double trapdoors.")
			.finishValue(enableTrapdoors::mirror)

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