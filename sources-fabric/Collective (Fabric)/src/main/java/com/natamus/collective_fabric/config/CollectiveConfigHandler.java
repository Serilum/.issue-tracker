/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.50.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.collective_fabric.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.collective_fabric.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class CollectiveConfigHandler {
	public static PropertyMirror<Boolean> transferItemsBetweenReplacedEntities = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> loopsAmountUsedToGetAllEntityDrops = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> findABlockcheckAroundEntitiesDelayMs = PropertyMirror.create(ConfigTypes.INTEGER);
	
	public static PropertyMirror<Boolean> enableAntiRepostingCheck = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enablePatronPets = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder()
			.beginValue("transferItemsBetweenReplacedEntities", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, transfer the held items and armour from replaced entities by any of the Entity Spawn mods which depend on Collective.")
			.finishValue(transferItemsBetweenReplacedEntities::mirror)
			
			.beginValue("loopsAmountUsedToGetAllEntityDrops", ConfigTypes.INTEGER, 100)
			.withComment("The amount of times Collective loops through possible mob drops to get them all procedurally. Drops are only generated when a dependent mod uses them. Lowering this can increase world load time but decrease accuracy.")
			.finishValue(loopsAmountUsedToGetAllEntityDrops::mirror)
			
			.beginValue("findABlockcheckAroundEntitiesDelayMs", ConfigTypes.INTEGER, 30000)
			.withComment("The delay of the is-there-a-block-around-check around entities in ms. Used in mods which depends on a specific blockstate in the world. Increasing this number can increase TPS if needed.")
			.finishValue(findABlockcheckAroundEntitiesDelayMs::mirror)
			
			
			.beginValue("enableAntiRepostingCheck", ConfigTypes.BOOLEAN, true)
			.withComment("Please check out https://stopmodreposts.org/ for more information on why this feature exists.")
			.finishValue(enableAntiRepostingCheck::mirror)

			.beginValue("enablePatronPets", ConfigTypes.BOOLEAN, true)
			.withComment("Enables pets for Patrons. Will be added in a future release.")
			.finishValue(enablePatronPets::mirror)

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