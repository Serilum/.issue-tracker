/*
 * This is the latest source code of Edibles.
 * Minecraft version: 1.19.x, mod version: 2.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Edibles ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.edibles.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.edibles.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Integer> maxItemUsesPerDaySingleItem = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> maxItemUsesPerDayTotal = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> weaknessDurationSeconds = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> glowEntityDurationSeconds = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> glowEntitiesAroundAffectedRadiusBlocks = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> _cooldownInMsBetweenUses = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> blazePowderStrengthDurationSeconds = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> magmaCreamFireResistanceDurationSeconds = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> sugarSpeedDurationSeconds = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> ghastTearDurationSeconds = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> phantomMembraneDurationSeconds = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> rabbitsFootDurationSeconds = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("maxItemUsesPerDaySingleItem", ConfigTypes.INTEGER, 16)
			.withComment("The maximum amount of an item a player can eat before receiving the weakness effect. A value of -1 disables this feature.")
			.finishValue(maxItemUsesPerDaySingleItem::mirror)

			.beginValue("maxItemUsesPerDayTotal", ConfigTypes.INTEGER, -1)
			.withComment("The maximum of the total amount of items a player can eat before receiving the weakness effect. A value of -1 disables this feature.")
			.finishValue(maxItemUsesPerDayTotal::mirror)

			.beginValue("weaknessDurationSeconds", ConfigTypes.INTEGER, 45)
			.withComment("The duration of the weakness effect in seconds when eating too much of an item.")
			.finishValue(weaknessDurationSeconds::mirror)

			.beginValue("glowEntityDurationSeconds", ConfigTypes.INTEGER, 20)
			.withComment("When eating glowstone, the duration in seconds of how long entities around should be glowing with an outline. A value of 0 disables the item use.")
			.finishValue(glowEntityDurationSeconds::mirror)

			.beginValue("glowEntitiesAroundAffectedRadiusBlocks", ConfigTypes.INTEGER, 32)
			.withComment("For the glow effect, the radius in blocks around the player of entities affected.")
			.finishValue(glowEntitiesAroundAffectedRadiusBlocks::mirror)

			.beginValue("_cooldownInMsBetweenUses", ConfigTypes.INTEGER, 1000)
			.withComment("The time in miliseconds of cooldown in between uses of eating an edible.")
			.finishValue(_cooldownInMsBetweenUses::mirror)

			.beginValue("blazePowderStrengthDurationSeconds", ConfigTypes.INTEGER, 15)
			.withComment("After eating blaze powder, the duration in seconds of the strength effect the player receives. A value of 0 disables the item use.")
			.finishValue(blazePowderStrengthDurationSeconds::mirror)

			.beginValue("magmaCreamFireResistanceDurationSeconds", ConfigTypes.INTEGER, 15)
			.withComment("After eating magma cream, the duration in seconds of the fire resistance effect the player receives. A value of 0 disables the item use.")
			.finishValue(magmaCreamFireResistanceDurationSeconds::mirror)

			.beginValue("sugarSpeedDurationSeconds", ConfigTypes.INTEGER, 15)
			.withComment("After eating some sugar, the duration in seconds of the speed effect the player receives. A value of 0 disables the item use.")
			.finishValue(sugarSpeedDurationSeconds::mirror)

			.beginValue("ghastTearDurationSeconds", ConfigTypes.INTEGER, 15)
			.withComment("After eating a ghast tear, the duration in seconds of the regeneration effect the player receives. A value of 0 disables the item use.")
			.finishValue(ghastTearDurationSeconds::mirror)

			.beginValue("phantomMembraneDurationSeconds", ConfigTypes.INTEGER, 15)
			.withComment("After eating some phantom membrane, the duration in seconds of the slow falling effect the player receives. A value of 0 disables the item use.")
			.finishValue(phantomMembraneDurationSeconds::mirror)

			.beginValue("rabbitsFootDurationSeconds", ConfigTypes.INTEGER, 15)
			.withComment("After eating a rabbit's foot, the duration in seconds of the jump boost effect the player receives. A value of 0 disables the item use.")
			.finishValue(rabbitsFootDurationSeconds::mirror)

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