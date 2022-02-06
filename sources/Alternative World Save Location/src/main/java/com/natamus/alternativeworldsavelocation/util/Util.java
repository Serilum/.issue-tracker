/*
 * This is the latest source code of Alternative World Save Location.
 * Minecraft version: 1.18.1, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Alternative World Save Location ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.alternativeworldsavelocation.util;

import java.io.File;
import java.util.regex.Matcher;

public class Util {
	public static String returnSystemSpecificPath(String path) {
		return path.replace("\\\\", "\\").replaceAll("(\\+|/+)", Matcher.quoteReplacement(File.separator));
	}
}