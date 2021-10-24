/*
 * This is the latest source code of GUI Followers.
 * Minecraft version: 1.17.x, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of GUI Followers ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.guifollowers.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.natamus.guifollowers.util.Reference;

import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;

public class ConfigHandler { 
	public static PropertyMirror<String> followerListHeaderFormat = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> showFollowerHealth = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> followerHealthFormat = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Boolean> showFollowerDistance = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<String> followerDistanceFormat = PropertyMirror.create(ConfigTypes.STRING);
	public static PropertyMirror<Integer> distanceToCheckForFollowersAround = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> timeBetweenChecksInSeconds = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Boolean> followerListPositionIsLeft = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> followerListPositionIsCenter = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Boolean> followerListPositionIsRight = PropertyMirror.create(ConfigTypes.BOOLEAN);
	public static PropertyMirror<Integer> followerListHeightOffset = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> RGB_R = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> RGB_G = PropertyMirror.create(ConfigTypes.INTEGER);
	public static PropertyMirror<Integer> RGB_B = PropertyMirror.create(ConfigTypes.INTEGER);

	private static final ConfigTree CONFIG = ConfigTree.builder() 
			.beginValue("followerListHeaderFormat", ConfigTypes.STRING, "Followers:")
			.withComment("The header text above the follower list.")
			.finishValue(followerListHeaderFormat::mirror)

			.beginValue("showFollowerHealth", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, shows the follower's health in the GUI list.")
			.finishValue(showFollowerHealth::mirror)

			.beginValue("followerHealthFormat", ConfigTypes.STRING, ": <health>%")
			.withComment("The format of the health string in the GUI. <health> will be replaced by the percentage of total health.")
			.finishValue(followerHealthFormat::mirror)

			.beginValue("showFollowerDistance", ConfigTypes.BOOLEAN, true)
			.withComment("If enabled, shows the follower's distance in blocks to the player in the GUI list.")
			.finishValue(showFollowerDistance::mirror)

			.beginValue("followerDistanceFormat", ConfigTypes.STRING, " (<distance> blocks)")
			.withComment("The format of the distance string in the GUI. <distance> will be replaced by distance in blocks.")
			.finishValue(followerDistanceFormat::mirror)

			.beginValue("distanceToCheckForFollowersAround", ConfigTypes.INTEGER, 50)
			.withComment("The distance in blocks around the player where the mod checks for tamed, non-sitting followers to add to the list. A value of -1 disables this feature.")
			.finishValue(distanceToCheckForFollowersAround::mirror)

			.beginValue("timeBetweenChecksInSeconds", ConfigTypes.INTEGER, 2)
			.withComment("The time in seconds in between checking for tamed, non-sitting followers around the player.")
			.finishValue(timeBetweenChecksInSeconds::mirror)

			.beginValue("followerListPositionIsLeft", ConfigTypes.BOOLEAN, true)
			.withComment("Places the follower list on the left.")
			.finishValue(followerListPositionIsLeft::mirror)

			.beginValue("followerListPositionIsCenter", ConfigTypes.BOOLEAN, false)
			.withComment("Places the follower list in the middle.")
			.finishValue(followerListPositionIsCenter::mirror)

			.beginValue("followerListPositionIsRight", ConfigTypes.BOOLEAN, false)
			.withComment("Places the follower list on the right.")
			.finishValue(followerListPositionIsRight::mirror)

			.beginValue("followerListHeightOffset", ConfigTypes.INTEGER, 20)
			.withComment("The vertical offset (y coord) for the follower list. This determines how far down the list should be on the screen. Can be changed to prevent GUIs from overlapping.")
			.finishValue(followerListHeightOffset::mirror)

			.beginValue("RGB_R", ConfigTypes.INTEGER, 255)
			.withComment("The red RGB value for the clock text.")
			.finishValue(RGB_R::mirror)

			.beginValue("RGB_G", ConfigTypes.INTEGER, 255)
			.withComment("The green RGB value for the clock text.")
			.finishValue(RGB_G::mirror)

			.beginValue("RGB_B", ConfigTypes.INTEGER, 255)
			.withComment("The blue RGB value for the clock text.")
			.finishValue(RGB_B::mirror)

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