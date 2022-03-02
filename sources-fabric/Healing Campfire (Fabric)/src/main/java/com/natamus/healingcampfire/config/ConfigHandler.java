/*
 * This is the latest source code of Healing Campfire.
 * Minecraft version: 1.19.x, mod version: 3.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Healing Campfire ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.healingcampfire.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.healingcampfire.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Integer> checkForCampfireDelayInTicks = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> healingRadius = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> effectDurationSeconds = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> regenerationLevel = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Boolean> healPassiveMobs = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> hideEffectParticles = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> campfireMustBeLit = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> campfireMustBeSignalling = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableEffectForNormalCampfires = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableEffectForSoulCampfires = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("checkForCampfireDelayInTicks", ConfigTypes.INTEGER, 40)
			.withComment("How often in ticks the mod checks for campfires around the player. 1 second = 20 ticks, so by default every 2 seconds.")
			.finishValue(checkForCampfireDelayInTicks::mirror)

			.beginValue("healingRadius", ConfigTypes.INTEGER, 16)
			.withComment("The radius around the campfire in blocks where players receive the regeneration effect.")
			.finishValue(healingRadius::mirror)

			.beginValue("effectDurationSeconds", ConfigTypes.INTEGER, 60)
			.withComment("The duration of the regeneration effect which the campfire applies.")
			.finishValue(effectDurationSeconds::mirror)

			.beginValue("regenerationLevel", ConfigTypes.INTEGER, 1)
			.withComment("The level of regeneration which the campfire applies, by default 1.")
			.finishValue(regenerationLevel::mirror)

			.beginValue("healPassiveMobs", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, the campfire heals passive mobs around where the radius is half the width of a bounding box.")
			.finishValue(healPassiveMobs::mirror)

			.beginValue("hideEffectParticles", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, hides the particles from the regeneration effect around the campfire.")
			.finishValue(hideEffectParticles::mirror)

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