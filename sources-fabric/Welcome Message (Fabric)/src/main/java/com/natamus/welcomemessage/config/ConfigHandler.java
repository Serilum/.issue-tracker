/*
 * This is the latest source code of Welcome Message.
 * Minecraft version: 1.19.1, mod version: 1.5.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.welcomemessage.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.welcomemessage.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> onlyRunOnDedicatedServers = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> sendEmptyLineBeforeFirstMessage = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> messageOneText = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Integer> messageOneColourIndex = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<String> messageOneOptionalURL = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> messageTwoText = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Integer> messageTwoColourIndex = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<String> messageTwoOptionalURL = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> messageThreeText = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Integer> messageThreeColourIndex = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<String> messageThreeOptionalURL = PropertyMirror.create(ConfigTypes.STRING);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("onlyRunOnDedicatedServers", ConfigTypes.BOOLEAN, true)
			.withComment("If the mod should only run on dedicated servers. When enabled it's not sent when in a singleplayer world.")
			.finishValue(onlyRunOnDedicatedServers::mirror)

			.beginValue("sendEmptyLineBeforeFirstMessage", ConfigTypes.BOOLEAN, true)
			.withComment("Whether an empty line should be send before to first message to separate the welcome from other chat messages that might be sent.")
			.finishValue(sendEmptyLineBeforeFirstMessage::mirror)

			.beginValue("messageOneText", ConfigTypes.STRING, "Welcome to the server!")
			.withComment("The first message a player will receive when joining the world. Can be left empty.")
			.finishValue(messageOneText::mirror)

			.beginValue("messageOneColourIndex", ConfigTypes.INTEGER, 2)
			.withComment("0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white")
			.finishValue(messageOneColourIndex::mirror)

			.beginValue("messageOneOptionalURL", ConfigTypes.STRING, "")
			.withComment("If a link is entered here, the complete message will be clickable.")
			.finishValue(messageOneOptionalURL::mirror)

			.beginValue("messageTwoText", ConfigTypes.STRING, "Downloaded from CurseForge! This is a clickable link.")
			.withComment("The second message a player will receive when joining the world. Can be left empty.")
			.finishValue(messageTwoText::mirror)

			.beginValue("messageTwoColourIndex", ConfigTypes.INTEGER, 14)
			.withComment("0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white")
			.finishValue(messageTwoColourIndex::mirror)

			.beginValue("messageTwoOptionalURL", ConfigTypes.STRING, "https://curseforge.com/members/serilum/projects")
			.withComment("If a link is entered here, the complete message will be clickable.")
			.finishValue(messageTwoOptionalURL::mirror)

			.beginValue("messageThreeText", ConfigTypes.STRING, "You should probably edit this in the config :)")
			.withComment("The third message a player will receive when joining the world. Can be left empty.")
			.finishValue(messageThreeText::mirror)

			.beginValue("messageThreeColourIndex", ConfigTypes.INTEGER, 15)
			.withComment("0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white")
			.finishValue(messageThreeColourIndex::mirror)

			.beginValue("messageThreeOptionalURL", ConfigTypes.STRING, "")
			.withComment("If a link is entered here, the complete message will be clickable.")
			.finishValue(messageThreeOptionalURL::mirror)

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