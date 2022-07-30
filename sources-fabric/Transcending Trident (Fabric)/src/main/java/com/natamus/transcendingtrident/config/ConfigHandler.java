/*
 * This is the latest source code of Transcending Trident.
 * Minecraft version: 1.19.1, mod version: 2.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Transcending Trident ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.transcendingtrident.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.transcendingtrident.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> mustHoldBucketOfWater = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> tridentUseDuration = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Double> tridentUsePowerModifier = PropertyMirror.create(ConfigTypes.DOUBLE);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("mustHoldBucketOfWater", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, Riptide can only be used without rain when the user is holding a bucket of water.")
			.finishValue(mustHoldBucketOfWater::mirror)

			.beginValue("tridentUseDuration", ConfigTypes.INTEGER, 5)
			.withComment("The amount of time a player needs to charge the trident before being able to use Riptide. Minecraft's default is 10.")
			.finishValue(tridentUseDuration::mirror)

			.beginValue("tridentUsePowerModifier", ConfigTypes.DOUBLE, 3.0)
			.withComment("The riptide power of the trident is multiplied by this number on use. Allows traveling a greater distance with a single charge.")
			.finishValue(tridentUsePowerModifier::mirror)

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