/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.2, mod version: 3.1.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.areas.config;

import com.natamus.areas.util.Reference;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ConfigHandler { 
	public static PropertyMirror<Boolean> giveUnnamedAreasRandomName = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> radiusAroundPlayerToCheckForSigns = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Boolean> sendChatMessages = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> showHUDMessages = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> joinPrefix = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> joinSuffix = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> leavePrefix = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<String> leaveSuffix = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> HUDOnlyAreaName = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> HUDMessageFadeDelayMs = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> HUDMessageHeightOffset = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Double> HUD_FontSizeScaleModifier = PropertyMirror.create(ConfigTypes.DOUBLE);
	public static PropertyMirror<Integer> HUD_RGB_R = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> HUD_RGB_G = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> HUD_RGB_B = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("giveUnnamedAreasRandomName", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, gives signs without an area name a randomly chosen one from a preset list.")
			.finishValue(giveUnnamedAreasRandomName::mirror)

			.beginValue("radiusAroundPlayerToCheckForSigns", ConfigTypes.INTEGER, 100)
			.withComment("The radius in blocks around the player in which to check for area signs.")
			.finishValue(radiusAroundPlayerToCheckForSigns::mirror)

			.beginValue("sendChatMessages", ConfigTypes.BOOLEAN, false)
			.withComment("When enabled, sends the player the area notifications in chat.")
			.finishValue(sendChatMessages::mirror)

			.beginValue("showHUDMessages", ConfigTypes.BOOLEAN, true)
			.withComment("When enabled, sends the player the area notifications in the HUD on screen.")
			.finishValue(showHUDMessages::mirror)

			.beginValue("joinPrefix", ConfigTypes.STRING, "Entering ")
			.withComment("The prefix of the message whenever a player enters an area.")
			.finishValue(joinPrefix::mirror)

			.beginValue("joinSuffix", ConfigTypes.STRING, ".")
			.withComment("The suffix of the message whenever a player enters an area.")
			.finishValue(joinSuffix::mirror)

			.beginValue("leavePrefix", ConfigTypes.STRING, "Leaving ")
			.withComment("The prefix of the message whenever a player leaves an area.")
			.finishValue(leavePrefix::mirror)

			.beginValue("leaveSuffix", ConfigTypes.STRING, ".")
			.withComment("The suffix of the message whenever a player leaves an area.")
			.finishValue(leaveSuffix::mirror)

			.beginValue("HUDOnlyAreaName", ConfigTypes.BOOLEAN, false)
			.withComment("When enabled, only shows the areaname in the HUD. When disabled, the prefixes and suffices will also be used.")
			.finishValue(HUDOnlyAreaName::mirror)

			.beginValue("HUDMessageFadeDelayMs", ConfigTypes.INTEGER, 4000)
			.withComment("The delay in ms after which the HUD message should fade out.")
			.finishValue(HUDMessageFadeDelayMs::mirror)

			.beginValue("HUDMessageHeightOffset", ConfigTypes.INTEGER, 10)
			.withComment("The vertical offset (y coord) for the HUD message. This determines how far down the message should be on the screen. Can be changed to prevent GUIs from overlapping.")
			.finishValue(HUDMessageHeightOffset::mirror)

			.beginValue("HUD_FontSizeScaleModifier", ConfigTypes.DOUBLE, 1.0)
			.withComment("Increases the font size of the text in the HUD message. If you change this value, make sure to test the different GUI scale settings in-game. 6.0 is considered large.")
			.finishValue(HUD_FontSizeScaleModifier::mirror)

			.beginValue("HUD_RGB_R", ConfigTypes.INTEGER, 100)
			.withComment("The red RGB value for the HUD message.")
			.finishValue(HUD_RGB_R::mirror)

			.beginValue("HUD_RGB_G", ConfigTypes.INTEGER, 200)
			.withComment("The green RGB value for the HUD message.")
			.finishValue(HUD_RGB_G::mirror)

			.beginValue("HUD_RGB_B", ConfigTypes.INTEGER, 50)
			.withComment("The blue RGB value for the HUD message.")
			.finishValue(HUD_RGB_B::mirror)

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