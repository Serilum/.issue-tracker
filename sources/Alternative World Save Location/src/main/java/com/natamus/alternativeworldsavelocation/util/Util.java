/*
 * This is the latest source code of Alternative World Save Location.
 * Minecraft version: 1.19.2, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.alternativeworldsavelocation.util;

import java.io.File;
import java.util.regex.Matcher;

public class Util {
	public static String returnSystemSpecificPath(String path) {
		return path.replace("\\\\", "\\").replaceAll("(\\+|/+)", Matcher.quoteReplacement(File.separator));
	}
}
