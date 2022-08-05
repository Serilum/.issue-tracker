/*
 * This is the latest source code of Altered Damage.
 * Minecraft version: 1.19.2, mod version: 2.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.altereddamage.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.altereddamage.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> preventFatalModifiedDamage = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> alterEntityDamageTaken = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> alterPlayerDamageTaken = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Double> entityDamageModifier = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Double> playerDamageModifier = PropertyMirror.create(ConfigTypes.DOUBLE);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("preventFatalModifiedDamage", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, does not change the damage output if it would be fatal with the modifier and not fatal without. Prevents dying from for example poison and starvation.")
			.finishValue(preventFatalModifiedDamage::mirror)

			.beginValue("alterEntityDamageTaken", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, modifies the damage another entity receives by the global modifier.")
			.finishValue(alterEntityDamageTaken::mirror)

			.beginValue("alterPlayerDamageTaken", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, modifies the damage a player receives by the global modifier.")
			.finishValue(alterPlayerDamageTaken::mirror)

			.beginValue("entityDamageModifier", ConfigTypes.DOUBLE, 2.0)
			.withComment("The global damage modifier for other entities.")
			.finishValue(entityDamageModifier::mirror)

			.beginValue("playerDamageModifier", ConfigTypes.DOUBLE, 2.0)
			.withComment("The global damage modifier for players.")
			.finishValue(playerDamageModifier::mirror)

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