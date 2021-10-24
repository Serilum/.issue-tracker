/*
 * This is the latest source code of Hoe Tweaks.
 * Minecraft version: 1.17.x, mod version: 1.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Hoe Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.hoetweaks.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.hoetweaks.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Double> cropBlockBreakSpeedModifier = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Boolean> mustCrouchToHaveBiggerHoeRange = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> woodenTierHoeRange = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> stoneTierHoeRange = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> goldTierHoeRange = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> ironTierHoeRange = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> diamondTierHoeRange = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> netheriteTierHoeRange = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("cropBlockBreakSpeedModifier", ConfigTypes.DOUBLE, 8.0)
			.withComment("How much quicker a cropblock (pumpkin/melon) is broken than by default.")
			.finishValue(cropBlockBreakSpeedModifier::mirror)

			.beginValue("mustCrouchToHaveBiggerHoeRange", ConfigTypes.BOOLEAN, true)
			.withComment("Whether the bigger hoe range should only be used if the player is crouching when right-clicking the center block.")
			.finishValue(mustCrouchToHaveBiggerHoeRange::mirror)

			.beginValue("woodenTierHoeRange", ConfigTypes.INTEGER, 0)
			.withComment("The wooden hoe till range (default while crouching). 0 = 1x1")
			.finishValue(woodenTierHoeRange::mirror)

			.beginValue("stoneTierHoeRange", ConfigTypes.INTEGER, 1)
			.withComment("The wooden hoe till range (default while crouching). 1 = 3x3")
			.finishValue(stoneTierHoeRange::mirror)

			.beginValue("goldTierHoeRange", ConfigTypes.INTEGER, 2)
			.withComment("The wooden hoe till range (default while crouching). 2 = 5x5")
			.finishValue(goldTierHoeRange::mirror)

			.beginValue("ironTierHoeRange", ConfigTypes.INTEGER, 2)
			.withComment("The wooden hoe till range (default while crouching). 2 = 5x5")
			.finishValue(ironTierHoeRange::mirror)

			.beginValue("diamondTierHoeRange", ConfigTypes.INTEGER, 3)
			.withComment("The wooden hoe till range (default while crouching). 3 = 7x7")
			.finishValue(diamondTierHoeRange::mirror)

			.beginValue("netheriteTierHoeRange", ConfigTypes.INTEGER, 4)
			.withComment("The wooden hoe till range (default while crouching). 4 = 9x9")
			.finishValue(netheriteTierHoeRange::mirror)

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