/*
 * This is the latest source code of Hide Hands.
 * Minecraft version: 1.17.x, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Hide Hands ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.hidehands.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.hidehands.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> alwaysHideMainHand = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> hideMainHandWithItems = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> alwaysHideOffhand = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> hideOffhandWithItems = PropertyMirror.create(ConfigTypes.STRING);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("alwaysHideMainHand", ConfigTypes.BOOLEAN, false)
			.withComment("If enabled, always hides the main hand. If disabled, hides the main hand when holding the items defined in hideMainHandWithItems.")
			.finishValue(alwaysHideMainHand::mirror)

			.beginValue("hideMainHandWithItems", ConfigTypes.STRING, "")
			.withComment("The items which when held will hide the main hand if alwaysHideMainHand is disabled.")
			.finishValue(hideMainHandWithItems::mirror)

			.beginValue("alwaysHideOffhand", ConfigTypes.BOOLEAN, false)
			.withComment("If enabled, always hides the offhand. If disabled, hides the offhand when holding the items defined in hideOffhandWithItems.")
			.finishValue(alwaysHideOffhand::mirror)

			.beginValue("hideOffhandWithItems", ConfigTypes.STRING, "minecraft:totem_of_undying,minecraft:torch")
			.withComment("The items which when held will hide the offhand if alwaysHideOffhand is disabled.")
			.finishValue(hideOffhandWithItems::mirror)

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