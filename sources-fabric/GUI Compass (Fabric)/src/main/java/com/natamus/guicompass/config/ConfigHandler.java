/*
 * This is the latest source code of GUI Compass.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.guicompass.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.guicompass.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler {
	public static PropertyMirror<String> guiCompassFormat = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> mustHaveCompassInInventory = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> compassPositionIsLeft = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> compassPositionIsCenter = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> compassPositionIsRight = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> compassHeightOffset = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> RGB_R = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> RGB_G = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> RGB_B = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder()
			.beginValue("guiCompassFormat", ConfigTypes.STRING, "FXYZ")
			.withComment("What of the GUI compass should be displayed. Default: [FXYZ]. F: facing (direction), X: x coord, Y: y coord (depth), Z: z coord.")
			.finishValue(guiCompassFormat::mirror)

			.beginValue("mustHaveCompassInInventory", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, will only show the GUI compass when a compass is present in the inventory.")
			.finishValue(mustHaveCompassInInventory::mirror)

			.beginValue("compassPositionIsLeft", ConfigTypes.BOOLEAN, true)
			.withComment("Places the GUI compass on the left.")
			.finishValue(compassPositionIsLeft::mirror)

			.beginValue("compassPositionIsCenter", ConfigTypes.BOOLEAN, false)
			.withComment("Places the GUI compass in the middle.")
			.finishValue(compassPositionIsCenter::mirror)

			.beginValue("compassPositionIsRight", ConfigTypes.BOOLEAN, false)
			.withComment("Places the GUI compass on the right.")
			.finishValue(compassPositionIsRight::mirror)

			.beginValue("compassHeightOffset", ConfigTypes.INTEGER, 5)
			.withComment("The vertical offset (y coord) for the Compass. This determines how far down the time should be on the screen. Can be changed to prevent GUIs from overlapping.")
			.finishValue(compassHeightOffset::mirror)

			.beginValue("RGB_R", ConfigTypes.INTEGER, 255)
			.withComment("The red RGB value for the compass text.")
			.finishValue(RGB_R::mirror)

			.beginValue("RGB_G", ConfigTypes.INTEGER, 255)
			.withComment("The green RGB value for the compass text.")
			.finishValue(RGB_G::mirror)

			.beginValue("RGB_B", ConfigTypes.INTEGER, 255)
			.withComment("The blue RGB value for the compass text.")
			.finishValue(RGB_B::mirror)

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