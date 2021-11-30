/*
 * This is the latest source code of Beautified Chat Server.
 * Minecraft version: 1.18.x, mod version: 1.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Beautified Chat Server ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.beautifiedchatserver.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.beautifiedchatserver.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<String> chatMessageFormat = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> timestampFormat = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Integer> chatTimestampColour = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> chatUsernameColour = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> chatMessageColour = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> chatOtherSymbolsColour = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("chatMessageFormat", ConfigTypes.STRING, "%timestamp% | %username%: %chatmessage%")
			.withComment("Variables: %timestamp% = timestamp set in timestampFormat. %username% = the username of the player who sent the message. %username% = the username of who sent the message.")
			.finishValue(chatMessageFormat::mirror)

			.beginValue("timestampFormat", ConfigTypes.STRING, "HH:mm")
			.withComment("Example time in formats: 5 seconds past 9 o' clock in the evening. *=Default. *(HH:mm) = 21:00, (HH:mm:ss) = 21:00:05, (hh:mm a) = 9:00 PM")
			.finishValue(timestampFormat::mirror)

			.beginValue("chatTimestampColour", ConfigTypes.INTEGER, 8)
			.withComment("The colour of the timestamp in the chat message. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
			.finishValue(chatTimestampColour::mirror)

			.beginValue("chatUsernameColour", ConfigTypes.INTEGER, 2)
			.withComment("The colour of the username in the chat messsage. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
			.finishValue(chatUsernameColour::mirror)

			.beginValue("chatMessageColour", ConfigTypes.INTEGER, 15)
			.withComment("The colour of the chat message. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
			.finishValue(chatMessageColour::mirror)

			.beginValue("chatOtherSymbolsColour", ConfigTypes.INTEGER, 7)
			.withComment("The colour of the other symbols from chatMessageFormat. So everything except the variables. The possible values are; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white.")
			.finishValue(chatOtherSymbolsColour::mirror)

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