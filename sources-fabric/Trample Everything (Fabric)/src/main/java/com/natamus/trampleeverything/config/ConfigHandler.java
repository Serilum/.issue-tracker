/*
 * This is the latest source code of Trample Everything.
 * Minecraft version: 1.19.2, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.trampleeverything.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.trampleeverything.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> _enableTrampledBlockItems = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> _crouchingPreventsTrampling = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleSnow = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleBambooSaplings = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleCrops = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleDeadBushes = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleDoublePlants = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleFlowers = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleFungi = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleLilyPads = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleMushrooms = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleNetherRoots = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleNetherSprouts = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleNetherWart = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleSaplings = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleSeaGrass = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleSeaPickles = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleStems = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleSugarCane = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleSweetBerryBushes = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> trampleTallGrass = PropertyMirror.create(ConfigTypes.BOOLEAN);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("_enableTrampledBlockItems", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, will drop blocks from a trampled block as if a player breaks it by hand.")
			.finishValue(_enableTrampledBlockItems::mirror)

			.beginValue("_crouchingPreventsTrampling", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, crouching/sneaking will prevent any block from being trampled.")
			.finishValue(_crouchingPreventsTrampling::mirror)

			.beginValue("trampleSnow", ConfigTypes.BOOLEAN, false)
			.withComment("Whether snow should be trampled.")
			.finishValue(trampleSnow::mirror)

			.beginValue("trampleBambooSaplings", ConfigTypes.BOOLEAN, false)
			.withComment("Whether bamboo saplings should be trampled.")
			.finishValue(trampleBambooSaplings::mirror)

			.beginValue("trampleCrops", ConfigTypes.BOOLEAN, true)
			.withComment("Whether growable crops should be trampled.")
			.finishValue(trampleCrops::mirror)

			.beginValue("trampleDeadBushes", ConfigTypes.BOOLEAN, true)
			.withComment("Whether dead bushes should be trampled")
			.finishValue(trampleDeadBushes::mirror)

			.beginValue("trampleDoublePlants", ConfigTypes.BOOLEAN, true)
			.withComment("Whether double plants should be trampled, for example tall flowers.")
			.finishValue(trampleDoublePlants::mirror)

			.beginValue("trampleFlowers", ConfigTypes.BOOLEAN, true)
			.withComment("Whether flowers should be trampled.")
			.finishValue(trampleFlowers::mirror)

			.beginValue("trampleFungi", ConfigTypes.BOOLEAN, true)
			.withComment("Whether nether mushrooms should be trampled.")
			.finishValue(trampleFungi::mirror)

			.beginValue("trampleLilyPads", ConfigTypes.BOOLEAN, false)
			.withComment("Whether lily pads should be trampled.")
			.finishValue(trampleLilyPads::mirror)

			.beginValue("trampleMushrooms", ConfigTypes.BOOLEAN, true)
			.withComment("Whether mushrooms should be trampled.")
			.finishValue(trampleMushrooms::mirror)

			.beginValue("trampleNetherRoots", ConfigTypes.BOOLEAN, true)
			.withComment("Whether nether roots should be trampled.")
			.finishValue(trampleNetherRoots::mirror)

			.beginValue("trampleNetherSprouts", ConfigTypes.BOOLEAN, true)
			.withComment("Whether nether sprouts should be trampled.")
			.finishValue(trampleNetherSprouts::mirror)

			.beginValue("trampleNetherWart", ConfigTypes.BOOLEAN, true)
			.withComment("Whether nether wart should be trampled.")
			.finishValue(trampleNetherWart::mirror)

			.beginValue("trampleSaplings", ConfigTypes.BOOLEAN, true)
			.withComment("Whether saplings should be trampled.")
			.finishValue(trampleSaplings::mirror)

			.beginValue("trampleSeaGrass", ConfigTypes.BOOLEAN, false)
			.withComment("Whether sea grass should be trampled.")
			.finishValue(trampleSeaGrass::mirror)

			.beginValue("trampleSeaPickles", ConfigTypes.BOOLEAN, true)
			.withComment("Whether sea pickles should be trampled.")
			.finishValue(trampleSeaPickles::mirror)

			.beginValue("trampleStems", ConfigTypes.BOOLEAN, true)
			.withComment("Whether stems should be trampled, such as pumpkin and melon stems.")
			.finishValue(trampleStems::mirror)

			.beginValue("trampleSugarCane", ConfigTypes.BOOLEAN, false)
			.withComment("Whether sugar cane should be trampled.")
			.finishValue(trampleSugarCane::mirror)

			.beginValue("trampleSweetBerryBushes", ConfigTypes.BOOLEAN, false)
			.withComment("Whether sweet berry bushes should be trampled.")
			.finishValue(trampleSweetBerryBushes::mirror)

			.beginValue("trampleTallGrass", ConfigTypes.BOOLEAN, true)
			.withComment("Whether tall grass should be trampled.")
			.finishValue(trampleTallGrass::mirror)

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