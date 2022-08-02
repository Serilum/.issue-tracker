/*
 * This is the latest source code of Eroding Stone Entities.
 * Minecraft version: 1.19.1, mod version: 2.8.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.erodingstoneentities.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.erodingstoneentities.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Integer> durationInSecondsStoneErodes = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Boolean> preventErosionIfAboveIceBlock = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> erodeIntoClayBlockInsteadOfClayBall = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> itemsWhichErodeIntoSand = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> itemsWhichErodeIntoRedSand = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> itemsWhichErodeIntoClay = PropertyMirror.create(ConfigTypes.STRING);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("durationInSecondsStoneErodes", ConfigTypes.INTEGER, 150)
			.withComment("The duration in seconds after a stone-type item entity in the world erodes to sand if it's in a water stream.")
			.finishValue(durationInSecondsStoneErodes::mirror)

			.beginValue("preventErosionIfAboveIceBlock", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, prevents the erosion of stone blocks if the item entity is above an ice block. Useful for when you use water streams to transport items you don't want to have eroded, just place ice underneath the streams.")
			.finishValue(preventErosionIfAboveIceBlock::mirror)

			.beginValue("erodeIntoClayBlockInsteadOfClayBall", ConfigTypes.BOOLEAN, false)
			.withComment("If enabled, the items specified in 'itemsWhichErodeIntoClay' will erode into a clay block instead of the default clay ball.")
			.finishValue(erodeIntoClayBlockInsteadOfClayBall::mirror)

			.beginValue("itemsWhichErodeIntoSand", ConfigTypes.STRING, "minecraft:cobblestone,minecraft:mossy_cobblestone,minecraft:stone,minecraft:stone_bricks,minecraft:chiseled_stone_bricks,minecraft:cracked_stone_bricks,minecraft:smooth_stone,minecraft:gravel,minecraft:andesite,minecraft:polished_andesite,minecraft:diorite,minecraft:polished_diorite,minecraft:granite,minecraft:polished_granite,minecraft:sandstone,minecraft:chiseled_sandstone,minecraft:cut_sandstone,minecraft:smooth_sandstone")
			.withComment("The items which erode into normal sand when left in flowing water. Divided by a comma.")
			.finishValue(itemsWhichErodeIntoSand::mirror)

			.beginValue("itemsWhichErodeIntoRedSand", ConfigTypes.STRING, "minecraft:red_sandstone,minecraft:chiseled_red_sandstone,minecraft:cut_red_sandstone,minecraft:smooth_red_sandstone,minecraft:netherrack,minecraft:nether_bricks,minecraft:red_nether_bricks")
			.withComment("The items which erode into red sand when left in flowing water. Divided by a comma.")
			.finishValue(itemsWhichErodeIntoRedSand::mirror)

			.beginValue("itemsWhichErodeIntoClay", ConfigTypes.STRING, "minecraft:terracotta,minecraft:white_terracotta,minecraft:orange_terracotta,minecraft:magenta_terracotta,minecraft:light_blue_terracotta,minecraft:yellow_terracotta,minecraft:lime_terracotta,minecraft:pink_terracotta,minecraft:gray_terracotta,minecraft:light_gray_terracotta,minecraft:cyan_terracotta,minecraft:purple_terracotta,minecraft:blue_terracotta,minecraft:brown_terracotta,minecraft:green_terracotta,minecraft:red_terracotta,minecraft:black_terracotta")
			.withComment("The items which erode into clay balls when left in flowing water. Divided by a comma.")
			.finishValue(itemsWhichErodeIntoClay::mirror)

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