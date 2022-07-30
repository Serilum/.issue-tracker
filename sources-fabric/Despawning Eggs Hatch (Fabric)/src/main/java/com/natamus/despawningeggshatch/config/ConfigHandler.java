/*
 * This is the latest source code of Despawning Eggs Hatch.
 * Minecraft version: 1.19.1, mod version: 2.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Despawning Eggs Hatch ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.despawningeggshatch.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.despawningeggshatch.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> eggOnlyHatchesWhenOnTopOfHayBlock = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Double> eggWillHatchChance = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Integer> onlyHatchIfLessChickensAroundThan = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> radiusEntityLimiterCheck = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Boolean> newHatchlingIsBaby = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("eggOnlyHatchesWhenOnTopOfHayBlock", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, an egg will only hatch when it is laid on top a hay block. This prevents wild chicken colonies from expanding without your knowledge.")
			.finishValue(eggOnlyHatchesWhenOnTopOfHayBlock::mirror)

			.beginValue("eggWillHatchChance", ConfigTypes.DOUBLE, 1.0)
			.withComment("The chance an egg will hatch just before despawning if the entity limiter is not active.")
			.finishValue(eggWillHatchChance::mirror)

			.beginValue("onlyHatchIfLessChickensAroundThan", ConfigTypes.INTEGER, 50)
			.withComment("Prevents too many entities from hatching. A despawning egg will only hatch if there are less chickens than defined here in a radius of 'radiusEntityLimiterCheck' blocks around.")
			.finishValue(onlyHatchIfLessChickensAroundThan::mirror)

			.beginValue("radiusEntityLimiterCheck", ConfigTypes.INTEGER, 32)
			.withComment("The radius around the despawned egg for 'onlyHatchIfLessChickensAroundThan'.")
			.finishValue(radiusEntityLimiterCheck::mirror)

			.beginValue("newHatchlingIsBaby", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, the newly hatched chicken is a small chick.")
			.finishValue(newHatchlingIsBaby::mirror)

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