/*
 * This is the latest source code of No Hostiles Around Campfire.
 * Minecraft version: 1.19.x, mod version: 4.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of No Hostiles Around Campfire ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.nohostilesaroundcampfire.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.nohostilesaroundcampfire.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Integer> preventHostilesRadius = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Boolean> burnHostilesAroundWhenPlaced = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Double> burnHostilesRadiusModifier = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Boolean> preventMobSpawnerSpawns = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> campfireMustBeLit = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> campfireMustBeSignalling = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableEffectForNormalCampfires = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableEffectForSoulCampfires = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("preventHostilesRadius", ConfigTypes.INTEGER, 48)
			.withComment("The radius around the campfire in blocks where hostile mob spawns will be blocked.")
			.finishValue(preventHostilesRadius::mirror)

			.beginValue("burnHostilesAroundWhenPlaced", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, burns all hostile mobs around the campfire within the radius whenever a player places a campfire.")
			.finishValue(burnHostilesAroundWhenPlaced::mirror)

			.beginValue("burnHostilesRadiusModifier", ConfigTypes.DOUBLE, 0.5)
			.withComment("By default set to 0.5. This means that if the radius is 16, the campfire burns prior mobs in a radius of 8.")
			.finishValue(burnHostilesRadiusModifier::mirror)

			.beginValue("preventMobSpawnerSpawns", ConfigTypes.BOOLEAN, false)
			.withComment("When enabled, the mob spawners spawns are also disabled when a campfire is within the radius.")
			.finishValue(preventMobSpawnerSpawns::mirror)

			.beginValue("campfireMustBeLit", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, the campfire only has an effect when the block is lit up.")
			.finishValue(campfireMustBeLit::mirror)

			.beginValue("campfireMustBeSignalling", ConfigTypes.BOOLEAN, false)
			.withComment("When enabled, the campfire only has an effect when the block is signalling.")
			.finishValue(campfireMustBeSignalling::mirror)

			.beginValue("enableEffectForNormalCampfires", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, the mod will work with normal campfires.")
			.finishValue(enableEffectForNormalCampfires::mirror)

			.beginValue("enableEffectForSoulCampfires", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, the mod will work with soul campfires.")
			.finishValue(enableEffectForSoulCampfires::mirror)

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