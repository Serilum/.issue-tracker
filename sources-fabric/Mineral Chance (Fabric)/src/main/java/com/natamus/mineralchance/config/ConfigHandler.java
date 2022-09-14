/*
 * This is the latest source code of Mineral Chance.
 * Minecraft version: 1.19.2, mod version: 2.1.
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

package com.natamus.mineralchance.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.mineralchance.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Double> extraMineralChanceOnOverworldStoneBreak = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Double> extraMineralChanceOnNetherStoneBreak = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Boolean> enableOverworldMinerals = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableNetherMinerals = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> sendMessageOnMineralFind = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> foundMineralMessage = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> ignoreFakePlayers = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("extraMineralChanceOnOverworldStoneBreak", ConfigTypes.DOUBLE, 0.02)
			.withComment("The chance a mineral is dropped when an overworld stone block is broken. By default 1/50.")
			.finishValue(extraMineralChanceOnOverworldStoneBreak::mirror)

			.beginValue("extraMineralChanceOnNetherStoneBreak", ConfigTypes.DOUBLE, 0.01)
			.withComment("The chance a mineral is dropped when a nether stone block is broken. By default 1/100.")
			.finishValue(extraMineralChanceOnNetherStoneBreak::mirror)

			.beginValue("enableOverworldMinerals", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, mining overworld stone blocks in the overworld has a chance to drop an overworld mineral. These consist of diamonds, gold nuggets, iron nuggets, lapis lazuli, redstone and emeralds.")
			.finishValue(enableOverworldMinerals::mirror)

			.beginValue("enableNetherMinerals", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, mining nether stone blocks in the nether has a chance to drop a nether mineral. These consist of quartz and gold nuggets.")
			.finishValue(enableNetherMinerals::mirror)

			.beginValue("sendMessageOnMineralFind", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, sends a message when a mineral is found to the player who broke the stone block.")
			.finishValue(sendMessageOnMineralFind::mirror)

			.beginValue("foundMineralMessage", ConfigTypes.STRING, "You've found a mineral hidden in the block!")
			.withComment("The message sent to the player who found a hidden mineral when 'sendMessageOnMineralFind' is enabled.")
			.finishValue(foundMineralMessage::mirror)

			.beginValue("ignoreFakePlayers", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, minerals won't be dropped if the player is a fake. For example when a mod breaks a block as a simulated player.")
			.finishValue(ignoreFakePlayers::mirror)

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