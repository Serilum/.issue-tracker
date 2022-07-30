/*
 * This is the latest source code of Player Death Kick.
 * Minecraft version: 1.19.1, mod version: 2.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Player Death Kick ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.playerdeathkick.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.playerdeathkick.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<String> disconnectMessage = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> addDeathCauseToMessage = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> exemptAdminPlayers = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> broadcastKickToServer = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("disconnectMessage", ConfigTypes.STRING, "You died by %death%! You have been disconnected from the server.")
			.withComment("The message players will receive when disconnected on death.")
			.finishValue(disconnectMessage::mirror)

			.beginValue("addDeathCauseToMessage", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, replaces %death% in the disconnect message with the death cause.")
			.finishValue(addDeathCauseToMessage::mirror)

			.beginValue("exemptAdminPlayers", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, exempts admin players (with cheat access, OPs) from being kicked on death.")
			.finishValue(exemptAdminPlayers::mirror)

			.beginValue("broadcastKickToServer", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, sends a message to all players online with who was kicked.")
			.finishValue(broadcastKickToServer::mirror)

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