/*
 * This is the latest source code of GUI Clock.
 * Minecraft version: 1.19.1, mod version: 3.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.guiclock.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.guiclock.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> mustHaveClockInInventoryForGameTime = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> mustHaveClockInInventoryForRealTime = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> lowerClockWhenPlayerHasEffects = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> _24hourformat = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> showOnlyMinecraftClockIcon = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> showBothTimes = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> showRealTime = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> showRealTimeSeconds = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> showDaysPlayedWorld = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> clockPositionIsLeft = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> clockPositionIsCenter = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> clockPositionIsRight = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> clockHeightOffset = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> clockWidthOffset = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> RGB_R = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> RGB_G = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> RGB_B = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("mustHaveClockInInventoryForGameTime", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, will only show the game time when a clock is present in the inventory.")
			.finishValue(mustHaveClockInInventoryForGameTime::mirror)

			.beginValue("mustHaveClockInInventoryForRealTime", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, will only show the real time when a clock is present in the inventory.")
			.finishValue(mustHaveClockInInventoryForRealTime::mirror)
			
			.beginValue("lowerClockWhenPlayerHasEffects", ConfigTypes.BOOLEAN, true)
			.withComment("Whether the clock in the GUI should be lowered when the player has potion effects to prevent overlap.")
			.finishValue(lowerClockWhenPlayerHasEffects::mirror)

			.beginValue("_24hourformat", ConfigTypes.BOOLEAN, true)
			.withComment("Sets the format of the clock to the 24-hour format.")
			.finishValue(_24hourformat::mirror)

			.beginValue("showOnlyMinecraftClockIcon", ConfigTypes.BOOLEAN, false)
			.withComment("When enabled, shows the clock item icon instead of a clock with numbers.")
			.finishValue(showOnlyMinecraftClockIcon::mirror)

			.beginValue("showBothTimes", ConfigTypes.BOOLEAN, false)
			.withComment("Show both in-game time and real local time.")
			.finishValue(showBothTimes::mirror)

			.beginValue("showRealTime", ConfigTypes.BOOLEAN, false)
			.withComment("Show actual local time instead of in-game time.")
			.finishValue(showRealTime::mirror)

			.beginValue("showRealTimeSeconds", ConfigTypes.BOOLEAN, false)
			.withComment("Show the seconds in the clock.")
			.finishValue(showRealTimeSeconds::mirror)

			.beginValue("showDaysPlayedWorld", ConfigTypes.BOOLEAN, true)
			.withComment("Show the days played in the world.")
			.finishValue(showDaysPlayedWorld::mirror)

			.beginValue("clockPositionIsLeft", ConfigTypes.BOOLEAN, false)
			.withComment("Places the GUI clock on the left.")
			.finishValue(clockPositionIsLeft::mirror)

			.beginValue("clockPositionIsCenter", ConfigTypes.BOOLEAN, false)
			.withComment("Places the GUI clock in the middle.")
			.finishValue(clockPositionIsCenter::mirror)

			.beginValue("clockPositionIsRight", ConfigTypes.BOOLEAN, true)
			.withComment("Places the GUI clock on the right.")
			.finishValue(clockPositionIsRight::mirror)

			.beginValue("clockHeightOffset", ConfigTypes.INTEGER, 5)
			.withComment("The vertical offset (y coord) for the Clock. This determines how far down the time should be on the screen. Can be changed to prevent GUIs from overlapping.")
			.finishValue(clockHeightOffset::mirror)

			.beginValue("clockWidthOffset", ConfigTypes.INTEGER, 0)
			.withComment("The horizontal offset (x coord) for the Clock.")
			.finishValue(clockWidthOffset::mirror)

			.beginValue("RGB_R", ConfigTypes.INTEGER, 255)
			.withComment("The red RGB value for the clock text.")
			.finishValue(RGB_R::mirror)

			.beginValue("RGB_G", ConfigTypes.INTEGER, 255)
			.withComment("The green RGB value for the clock text.")
			.finishValue(RGB_G::mirror)

			.beginValue("RGB_B", ConfigTypes.INTEGER, 255)
			.withComment("The blue RGB value for the clock text.")
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