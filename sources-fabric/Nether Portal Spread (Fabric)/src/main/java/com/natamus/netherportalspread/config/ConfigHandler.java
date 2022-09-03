/*
 * This is the latest source code of Nether Portal Spread.
 * Minecraft version: 1.19.2, mod version: 6.3.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.netherportalspread.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.netherportalspread.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> sendMessageOnPortalCreation = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> messageOnPortalCreation = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> sendMessageOnPreventSpreadBlocksFound = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> messageOnPreventSpreadBlocksFound = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> sendMessageOnPortalBroken = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> messageOnPortalBroken = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> prefixPortalCoordsInMessage = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> portalSpreadRadius = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> spreadDelayTicks = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> instantConvertAmount = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Boolean> preventSpreadWithBlock = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> preventSpreadBlockAmountNeeded = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<String> preventSpreadBlockString = PropertyMirror.create(ConfigTypes.STRING);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("sendMessageOnPortalCreation", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, sends a message to players around the portal that the nether is spreading and that you can stop the spread with 'preventSpreadBlockAmountNeeded' of the 'preventSpreadBlockString' block.")
			.finishValue(sendMessageOnPortalCreation::mirror)

			.beginValue("messageOnPortalCreation", ConfigTypes.STRING, "You feel a corrupted energy coming from the portal. The nether will slowly spread into the overworld unless %preventSpreadBlockAmountNeeded% %preventSpreadBlockString% are placed within a %portalSpreadRadius% block radius around the portal.")
			.withComment("The message sent on portal creation.")
			.finishValue(messageOnPortalCreation::mirror)

			.beginValue("sendMessageOnPreventSpreadBlocksFound", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, sends a message to players around the portal that the nether spread has stopped when the portal detects new 'preventSpreadBlockString' blocks.")
			.finishValue(sendMessageOnPreventSpreadBlocksFound::mirror)

			.beginValue("messageOnPreventSpreadBlocksFound", ConfigTypes.STRING, "With enough %preventSpreadBlockString% placed, you feel the corrupted energy fade.")
			.withComment("The message sent on preventspread blocks found.")
			.finishValue(messageOnPreventSpreadBlocksFound::mirror)

			.beginValue("sendMessageOnPortalBroken", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, sends a message to players around the portal when it is broken.")
			.finishValue(sendMessageOnPortalBroken::mirror)

			.beginValue("messageOnPortalBroken", ConfigTypes.STRING, "With the nether portal broken, the corrupted energy is no longer able to enter the overworld.")
			.withComment("The message sent when a portal is broken.")
			.finishValue(messageOnPortalBroken::mirror)

			.beginValue("prefixPortalCoordsInMessage", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, shows the portal coordinates in portal messages.")
			.finishValue(prefixPortalCoordsInMessage::mirror)

			.beginValue("portalSpreadRadius", ConfigTypes.INTEGER, 30)
			.withComment("The radius around the portal to which the nether blocks can spread.")
			.finishValue(portalSpreadRadius::mirror)

			.beginValue("spreadDelayTicks", ConfigTypes.INTEGER, 40)
			.withComment("The delay in ticks in between the spread around the nether portal. 20 ticks = 1 second.")
			.finishValue(spreadDelayTicks::mirror)

			.beginValue("instantConvertAmount", ConfigTypes.INTEGER, 50)
			.withComment("The amount of blocks that are instantly converted to a nether block around a portal when it is detected. If there are existing nether blocks within the radius, their count is substracted from this number.")
			.finishValue(instantConvertAmount::mirror)

			.beginValue("preventSpreadWithBlock", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, blocks the spread effect when there are n (defined) prevent-spread-blocks (defined) within the radius.")
			.finishValue(preventSpreadWithBlock::mirror)

			.beginValue("preventSpreadBlockAmountNeeded", ConfigTypes.INTEGER, 4)
			.withComment("The amount of prevent-spread-blocks (defined) needed within the radius of the nether portal to prevent spread.")
			.finishValue(preventSpreadBlockAmountNeeded::mirror)

			.beginValue("preventSpreadBlockString", ConfigTypes.STRING, "minecraft:coal_block")
			.withComment("The block which prevents the nether portal from spreading. By default a coal block (minecraft:coal_block is the namespace ID).")
			.finishValue(preventSpreadBlockString::mirror)

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