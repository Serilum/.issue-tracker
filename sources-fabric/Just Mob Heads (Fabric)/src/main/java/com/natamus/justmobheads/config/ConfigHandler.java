/*
 * This is the latest source code of Just Mob Heads.
 * Minecraft version: 1.19.2, mod version: 5.9.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.justmobheads.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.justmobheads.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> mobSpecificDropChances = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableStandardHeads = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> enableLootingEnchant = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> onlyAdultMobsDropTheirHead = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Double> overallDropChance = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Double> creeperSkeletonZombieDropChance = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Boolean> onlyDropHeadsByChargedCreeper = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> onlyDropHeadsByPlayerKill = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("mobSpecificDropChances", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, overrides the 'overallDropChance' variable with the specific values.")
			.finishValue(mobSpecificDropChances::mirror)

			.beginValue("enableStandardHeads", ConfigTypes.BOOLEAN, false)
			.withComment("If enabled, allows Creepers, Skeletons and Zombies to drop their heads.")
			.finishValue(enableStandardHeads::mirror)

			.beginValue("enableLootingEnchant", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, the looting enchant will have an effect on the drop chance.")
			.finishValue(enableLootingEnchant::mirror)

			.beginValue("onlyAdultMobsDropTheirHead", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, only adult tameable mobs will have a chance to drop their head on death.")
			.finishValue(onlyAdultMobsDropTheirHead::mirror)

			.beginValue("overallDropChance", ConfigTypes.DOUBLE, 0.1)
			.withComment("Sets the chance of a mob dropping its head if 'mobSpecificDropChances' is disabled.")
			.finishValue(overallDropChance::mirror)

			.beginValue("creeperSkeletonZombieDropChance", ConfigTypes.DOUBLE, 0.1)
			.withComment("Sets head drop chance for Zombies, Skeletons and Creepers if 'enableStandardHeads' is enabled.")
			.finishValue(creeperSkeletonZombieDropChance::mirror)
			
			.beginValue("onlyDropHeadsByChargedCreeper", ConfigTypes.BOOLEAN, false)
			.withComment("When enabled, only drops mob heads if the source on death is a charged creeper. This overwrites the onlyDropHeadsByPlayerKill value.")
			.finishValue(onlyDropHeadsByChargedCreeper::mirror)
			
			.beginValue("onlyDropHeadsByPlayerKill", ConfigTypes.BOOLEAN, false)
			.withComment("When enabled, only drops mob heads if the source on death is from a player.")
			.finishValue(onlyDropHeadsByPlayerKill::mirror)

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