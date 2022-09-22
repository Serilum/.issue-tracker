/*
 * This is the latest source code of End Portal Recipe.
 * Minecraft version: 1.19.2, mod version: 4.0.
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

package com.natamus.endportalrecipe.config;

import com.natamus.endportalrecipe.util.Reference;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ConfigHandler {
	public static PropertyMirror<Boolean> mustHaveSilkTouchToBreakPortal = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> addBrokenPortalFramesToInventory = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> sendMessageOnExtraDragonEggDrop = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder()
			.beginValue("mustHaveSilkTouchToBreakPortal", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, players can only break portal frames if they have silk touch on their pickaxe.")
			.finishValue(mustHaveSilkTouchToBreakPortal::mirror)

			.beginValue("addBrokenPortalFramesToInventory", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, add portal frames directly to the player's inventory to lower the chance of them being destroyed. Still drops the item entity if the inventory is full.")
			.finishValue(addBrokenPortalFramesToInventory::mirror)

			.beginValue("sendMessageOnExtraDragonEggDrop", ConfigTypes.BOOLEAN, true)
			.withComment("Whether a message should be sent to the player where the extra dragon egg will drop.")
			.finishValue(sendMessageOnExtraDragonEggDrop::mirror)

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