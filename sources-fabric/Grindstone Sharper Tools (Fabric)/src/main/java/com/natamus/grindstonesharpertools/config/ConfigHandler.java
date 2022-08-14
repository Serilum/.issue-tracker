/*
 * This is the latest source code of Grindstone Sharper Tools.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.grindstonesharpertools.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.grindstonesharpertools.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Integer> usesAfterGrinding = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Double> sharpenedDamageModifier = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Boolean> infiniteCreativeUses = PropertyMirror.create(ConfigTypes.BOOLEAN);

	public static PropertyMirror<Boolean> sendUsesLeftInChat = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> showUsesLeftInItemName = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> nameUsesPrefix = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> nameUsesSuffix = PropertyMirror.create(ConfigTypes.STRING);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("usesAfterGrinding", ConfigTypes.INTEGER, 32)
			.withComment("The amount of sharper uses a tool has after using it on the grindstone.")
			.finishValue(usesAfterGrinding::mirror)

			.beginValue("sharpenedDamageModifier", ConfigTypes.DOUBLE, 1.5)
			.withComment("The damage modifier of sharpened tools.")
			.finishValue(sharpenedDamageModifier::mirror)

			.beginValue("infiniteCreativeUses", ConfigTypes.BOOLEAN, false)
			.withComment("Whether to decrease sharpened uses in creative.")
			.finishValue(infiniteCreativeUses::mirror)

			.beginValue("sendUsesLeftInChat", ConfigTypes.BOOLEAN, true)
			.withComment("Sends the sharpened tool user a message at 75%, 50%, 25%, 10%.")
			.finishValue(sendUsesLeftInChat::mirror)

			.beginValue("showUsesLeftInItemName", ConfigTypes.BOOLEAN, true)
			.withComment("Shows the uses left in the name of the item.")
			.finishValue(showUsesLeftInItemName::mirror)

			.beginValue("nameUsesPrefix", ConfigTypes.STRING, "(Sharpened uses: ")
			.withComment("The prefix of the sharpened uses left in the tool name.")
			.finishValue(nameUsesPrefix::mirror)

			.beginValue("nameUsesSuffix", ConfigTypes.STRING, ")")
			.withComment("The suffix of the sharpened uses left in the tool name.")
			.finishValue(nameUsesSuffix::mirror)

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