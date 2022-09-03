/*
 * This is the latest source code of Respawn Delay.
 * Minecraft version: 1.19.2, mod version: 3.1.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.respawndelay.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.respawndelay.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> ignoreAdministratorPlayers = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> ignoreCreativePlayers = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> keepItemsOnDeath = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> dropItemsOnDeath = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> respawnAtWorldSpawn = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> respawnWhenPlayerLogsOut = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> respawnWhenPlayerLogsIn = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> respawnDelayInSeconds = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<String> onDeathMessage = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> onRespawnMessage = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> waitingForRespawnMessage = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Integer> lowestPossibleYCoordinate = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("ignoreAdministratorPlayers", ConfigTypes.BOOLEAN, false)
			.withComment("If enabled, player operators/administrators will not be put in spectator mode on death.")
			.finishValue(ignoreAdministratorPlayers::mirror)

			.beginValue("ignoreCreativePlayers", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, player in creative mode will not be put in spectator mode on death.")
			.finishValue(ignoreCreativePlayers::mirror)

			.beginValue("keepItemsOnDeath", ConfigTypes.BOOLEAN, false)
			.withComment("If enabled, players will keep their items on death, and no drop event will be ran. This will also ignore the 'dropItemsOnDeath' config.")
			.finishValue(keepItemsOnDeath::mirror)

			.beginValue("dropItemsOnDeath", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, players will drop their items on death as normal vanilla behaviour.")
			.finishValue(dropItemsOnDeath::mirror)

			.beginValue("respawnAtWorldSpawn", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, players will be respawned at the world spawn point. If disabled, they'll respawn wherever they're spectating.")
			.finishValue(respawnAtWorldSpawn::mirror)

			.beginValue("respawnWhenPlayerLogsOut", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, players will respawn when they logout while waiting for a respawn. Prevents players from getting stuck in spectator mode.")
			.finishValue(respawnWhenPlayerLogsOut::mirror)

			.beginValue("respawnWhenPlayerLogsIn", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, players will respawn when they log in and are still in spectator mode. Prevents players from getting stuck in spectator mode.")
			.finishValue(respawnWhenPlayerLogsIn::mirror)

			.beginValue("respawnDelayInSeconds", ConfigTypes.INTEGER, 10)
			.withComment("The delay in seconds after which a spectating player will respawn. A value of -1 makes players never respawn automatically. The '/respawnall' command can still be used.")
			.finishValue(respawnDelayInSeconds::mirror)

			.beginValue("onDeathMessage", ConfigTypes.STRING, "You died! Your gamemode has been set to spectator.")
			.withComment("The message which is sent to the player on death. Leave empty to disable.")
			.finishValue(onDeathMessage::mirror)

			.beginValue("onRespawnMessage", ConfigTypes.STRING, "You respawned.")
			.withComment("The message which is sent to players when they respawn. Leave empty to disable.")
			.finishValue(onRespawnMessage::mirror)

			.beginValue("waitingForRespawnMessage", ConfigTypes.STRING, "You will respawn in <seconds_left> seconds.")
			.withComment("The message which is sent to players when they are waiting to be respawned. The text '<seconds_left>' will be replaced with the actual seconds left. Leave empty to disable.")
			.finishValue(waitingForRespawnMessage::mirror)

			.beginValue("lowestPossibleYCoordinate", ConfigTypes.INTEGER, -10)
			.withComment("When a player falls into the void, this determines the y position that's set after when a player enters spectator mode.")
			.finishValue(lowestPossibleYCoordinate::mirror)

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