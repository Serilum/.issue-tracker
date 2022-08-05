/*
 * This is the latest source code of Campfire Spawn and Tweaks.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.campfirespawnandtweaks.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.campfirespawnandtweaks.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler {
	public static PropertyMirror<Boolean> campfiresStartUnlit = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> sneakRightClickCampfireToUnset = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> bedsOverrideCampfireSpawnOnSneakRightClick = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> createAirPocketIfBlocksAboveCampfire = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> sendMessageOnNewCampfireSpawnSet = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> sendMessageOnCampfireSpawnUnset = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> sendMessageOnCampfireSpawnMissing = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> sendMessageOnCampfireSpawnOverride = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> fireResitanceDurationOnRespawnInMs = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder()
			.beginValue("campfiresStartUnlit", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, a newly placed campfire will be unlit.")
			.finishValue(campfiresStartUnlit::mirror)
			
			.beginValue("sneakRightClickCampfireToUnset", ConfigTypes.BOOLEAN, true)
			.withComment("Crouching/Sneaking and right-clicking on a campfire unsets the campfire spawn point.")
			.finishValue(sneakRightClickCampfireToUnset::mirror)

			.beginValue("bedsOverrideCampfireSpawnOnSneakRightClick", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, sneak/crouch + right-clicking a bed will override the campfire spawn point.")
			.finishValue(bedsOverrideCampfireSpawnOnSneakRightClick::mirror)

			.beginValue("createAirPocketIfBlocksAboveCampfire", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, the mod breaks the blocks above a campfire on respawn if it would somehow be blocked.")
			.finishValue(createAirPocketIfBlocksAboveCampfire::mirror)

			.beginValue("sendMessageOnNewCampfireSpawnSet", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, a message will be sent to the player whenever a new campfire spawn point is set.")
			.finishValue(sendMessageOnNewCampfireSpawnSet::mirror)

			.beginValue("sendMessageOnCampfireSpawnUnset", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, a message will be sent to the player whenever a campfire spawn point is unset.")
			.finishValue(sendMessageOnCampfireSpawnUnset::mirror)

			.beginValue("sendMessageOnCampfireSpawnMissing", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, a message will be sent to the player whenever a campfire spawn point is missing on respawn.")
			.finishValue(sendMessageOnCampfireSpawnMissing::mirror)

			.beginValue("sendMessageOnCampfireSpawnOverride", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, a message will be sent to the player whenever a campfire spawn point is overridden by the PlayerSetSpawnEvent.")
			.finishValue(sendMessageOnCampfireSpawnOverride::mirror)

			.beginValue("fireResitanceDurationOnRespawnInMs", ConfigTypes.INTEGER, 10000)
			.withComment("The duration of fire resistance when a player respawns at a campfire. A value of 0 disables this feature, and places the player next to the campfire instead.")
			.finishValue(fireResitanceDurationOnRespawnInMs::mirror)

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