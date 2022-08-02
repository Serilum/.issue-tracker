/*
 * This is the latest source code of World Border.
 * Minecraft version: 1.19.1, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.worldborder.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.worldborder.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> enableCustomOverworldBorder = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableCustomNetherBorder = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableCustomEndBorder = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> shouldLoopToOppositeBorder = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> distanceTeleportedBack = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<String> nearBorderMessage = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> hitBorderMessage = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> loopBorderMessage = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Integer> overworldBorderPositiveX = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> overworldBorderNegativeX = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> overworldBorderPositiveZ = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> overworldBorderNegativeZ = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> netherBorderPositiveX = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> netherBorderNegativeX = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> netherBorderPositiveZ = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> netherBorderNegativeZ = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> endBorderPositiveX = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> endBorderNegativeX = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> endBorderPositiveZ = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> endBorderNegativeZ = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("enableCustomOverworldBorder", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, uses the overworldBorderCoords to set the border.")
			.finishValue(enableCustomOverworldBorder::mirror)

			.beginValue("enableCustomNetherBorder", ConfigTypes.BOOLEAN, false)
			.withComment("When enabled, uses the netherBorderCoords to set the border.")
			.finishValue(enableCustomNetherBorder::mirror)

			.beginValue("enableCustomEndBorder", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, uses the endBorderCoords to set the border.")
			.finishValue(enableCustomEndBorder::mirror)

			.beginValue("shouldLoopToOppositeBorder", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, instead of teleporting the player inside near where they were, teleports them from the positive to the negative x/z coord and vice versa.")
			.finishValue(shouldLoopToOppositeBorder::mirror)

			.beginValue("distanceTeleportedBack", ConfigTypes.INTEGER, 10)
			.withComment("The amount of blocks the player is teleported inside after hitting the border.")
			.finishValue(distanceTeleportedBack::mirror)

			.beginValue("nearBorderMessage", ConfigTypes.STRING, "You're getting close to the world border!")
			.withComment("The message which will be sent to the player when they are within 'distanceTeleportedBack' to the world border.")
			.finishValue(nearBorderMessage::mirror)

			.beginValue("hitBorderMessage", ConfigTypes.STRING, "You've hit the world border, and were teleported inside!")
			.withComment("The message which will be sent to the player when they hit the world border.")
			.finishValue(hitBorderMessage::mirror)

			.beginValue("loopBorderMessage", ConfigTypes.STRING, "You've hit the world border, and have looped around the world!")
			.withComment("The message sent to the player when they hit the border and 'shouldLoopToOppositeBorder' is enabled.")
			.finishValue(loopBorderMessage::mirror)

			.beginValue("overworldBorderPositiveX", ConfigTypes.INTEGER, 5000)
			.withComment("The overworld border located at the positive x coordinate.")
			.finishValue(overworldBorderPositiveX::mirror)

			.beginValue("overworldBorderNegativeX", ConfigTypes.INTEGER, -5000)
			.withComment("The overworld border located at the negative x coordinate.")
			.finishValue(overworldBorderNegativeX::mirror)

			.beginValue("overworldBorderPositiveZ", ConfigTypes.INTEGER, 5000)
			.withComment("The overworld border located at the positive z coordinate.")
			.finishValue(overworldBorderPositiveZ::mirror)

			.beginValue("overworldBorderNegativeZ", ConfigTypes.INTEGER, -5000)
			.withComment("The overworld border located at the negative z coordinate.")
			.finishValue(overworldBorderNegativeZ::mirror)

			.beginValue("netherBorderPositiveX", ConfigTypes.INTEGER, 625)
			.withComment("The nether border located at the positive x coordinate.")
			.finishValue(netherBorderPositiveX::mirror)

			.beginValue("netherBorderNegativeX", ConfigTypes.INTEGER, -625)
			.withComment("The nether border located at the negative x coordinate.")
			.finishValue(netherBorderNegativeX::mirror)

			.beginValue("netherBorderPositiveZ", ConfigTypes.INTEGER, 625)
			.withComment("The nether border located at the positive z coordinate.")
			.finishValue(netherBorderPositiveZ::mirror)

			.beginValue("netherBorderNegativeZ", ConfigTypes.INTEGER, -625)
			.withComment("The nether border located at the negative z coordinate.")
			.finishValue(netherBorderNegativeZ::mirror)

			.beginValue("endBorderPositiveX", ConfigTypes.INTEGER, 5000)
			.withComment("The end border located at the positive x coordinate.")
			.finishValue(endBorderPositiveX::mirror)

			.beginValue("endBorderNegativeX", ConfigTypes.INTEGER, -5000)
			.withComment("The end border located at the negative x coordinate.")
			.finishValue(endBorderNegativeX::mirror)

			.beginValue("endBorderPositiveZ", ConfigTypes.INTEGER, 5000)
			.withComment("The end border located at the positive z coordinate.")
			.finishValue(endBorderPositiveZ::mirror)

			.beginValue("endBorderNegativeZ", ConfigTypes.INTEGER, -5000)
			.withComment("The end border located at the negative z coordinate.")
			.finishValue(endBorderNegativeZ::mirror)

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