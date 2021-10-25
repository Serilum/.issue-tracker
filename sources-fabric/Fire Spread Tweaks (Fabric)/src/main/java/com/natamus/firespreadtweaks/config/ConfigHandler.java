/*
 * This is the latest source code of Fire Spread Tweaks.
 * Minecraft version: 1.17.x, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Fire Spread Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.firespreadtweaks.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.firespreadtweaks.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Integer> timeFireBurnsInTicks = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Boolean> enableRandomizedFireDuration = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> MinRandomExtraBurnTicks = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> MaxRandomExtraBurnTicks = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("timeFireBurnsInTicks", ConfigTypes.INTEGER, 300)
			.withComment("The time a fire will keep burning for in ticks. 20 ticks = 1 second.")
			.finishValue(timeFireBurnsInTicks::mirror)

			.beginValue("enableRandomizedFireDuration", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, uses the MinRandomExtraBurnTicks and MaxRandomExtraBurnTicks config values to randomize the time fire burns for.")
			.finishValue(enableRandomizedFireDuration::mirror)

			.beginValue("MinRandomExtraBurnTicks", ConfigTypes.INTEGER, -100)
			.withComment("If randomized fire duration is enabled, a random tick number will be chosen in between the minimum and maximum value. This will be added to timeFireBurnsInTicks. If the outcome is negative, it will be subtracted.")
			.finishValue(MinRandomExtraBurnTicks::mirror)

			.beginValue("MaxRandomExtraBurnTicks", ConfigTypes.INTEGER, 100)
			.withComment("See MinRandomExtraBurnTicks's description.")
			.finishValue(MaxRandomExtraBurnTicks::mirror)

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