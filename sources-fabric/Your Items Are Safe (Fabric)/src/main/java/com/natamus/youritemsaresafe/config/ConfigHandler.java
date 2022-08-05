/*
 * This is the latest source code of Your Items Are Safe.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.youritemsaresafe.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.youritemsaresafe.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> mustHaveItemsInInventoryForCreation = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> addPlayerHeadToArmorStand = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> createArmorStand = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> createSignWithPlayerName = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> needChestMaterials = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> needArmorStandMaterials = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> needSignMaterials = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> ignoreStoneMaterialNeed = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> sendMessageOnCreationFailure = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> sendMessageOnCreationSuccess = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> creationFailureMessage = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> creationSuccessMessage = PropertyMirror.create(ConfigTypes.STRING);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("mustHaveItemsInInventoryForCreation", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled and a player dies without any items in their inventory, no chest or armor stand is generated.")
			.finishValue(mustHaveItemsInInventoryForCreation::mirror)

			.beginValue("addPlayerHeadToArmorStand", ConfigTypes.BOOLEAN, true)
			.withComment("If a player head should be added to the armor stand. If a helmet is worn, this will be placed into the chest.")
			.finishValue(addPlayerHeadToArmorStand::mirror)

			.beginValue("createArmorStand", ConfigTypes.BOOLEAN, true)
			.withComment("Whether an armor stand should be created on death. If disabled, the player's gear will be placed inside the chest.")
			.finishValue(createArmorStand::mirror)

			.beginValue("createSignWithPlayerName", ConfigTypes.BOOLEAN, true)
			.withComment("Whether a sign should be placed on the chest with the name of the player who died there.")
			.finishValue(createSignWithPlayerName::mirror)

			.beginValue("needChestMaterials", ConfigTypes.BOOLEAN, true)
			.withComment("Whether materials are needed for the chest which spawns on death. This can be the actual chest or the costs in raw materials.")
			.finishValue(needChestMaterials::mirror)

			.beginValue("needArmorStandMaterials", ConfigTypes.BOOLEAN, true)
			.withComment("Whether materials are needed for the armor stand to spawn on death. This can be the actual armor stand or the costs in raw materials.")
			.finishValue(needArmorStandMaterials::mirror)

			.beginValue("needSignMaterials", ConfigTypes.BOOLEAN, false)
			.withComment("Whether materials are needed for the creation of the sign when 'createSignWithPlayerName' is enabled.")
			.finishValue(needSignMaterials::mirror)

			.beginValue("ignoreStoneMaterialNeed", ConfigTypes.BOOLEAN, true)
			.withComment("Only relevant if 'needChestAndArmorStandMaterials' is enabled. An armor stand needs 1 stone slab to be created, but I think it's alright to ignore that requirement. If enabled, no stone is needed in the inventory on death.")
			.finishValue(ignoreStoneMaterialNeed::mirror)

			.beginValue("sendMessageOnCreationFailure", ConfigTypes.BOOLEAN, true)
			.withComment("If a message should be sent if the chest or armor stand can't be created due to missing materials.")
			.finishValue(sendMessageOnCreationFailure::mirror)

			.beginValue("sendMessageOnCreationSuccess", ConfigTypes.BOOLEAN, true)
			.withComment("If a message should be sent on successful creation of the chest(s) and armor stand.")
			.finishValue(sendMessageOnCreationSuccess::mirror)

			.beginValue("creationFailureMessage", ConfigTypes.STRING, "Your items are not safe due to having insufficient materials. Missing: %plankamount% planks.")
			.withComment("The message sent on creation failure with 'sendMessageOnCreationFailure' enabled. Possible replacement values: %plankamount%, %stoneamount%.")
			.finishValue(creationFailureMessage::mirror)

			.beginValue("creationSuccessMessage", ConfigTypes.STRING, "Your items are safe at your death location.")
			.withComment("The message sent on creation success with 'sendMessageOnCreationSuccess' enabled.")
			.finishValue(creationSuccessMessage::mirror)

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