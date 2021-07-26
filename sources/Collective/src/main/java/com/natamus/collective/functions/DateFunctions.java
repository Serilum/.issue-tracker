/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.1, mod version: 2.35.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFunctions {
	public static String ymdhisToReadable(String ymdhis) {
		DateFormat ymdhisformat = new SimpleDateFormat("yyyyMMddhhmmss");
		DateFormat readableformat = new SimpleDateFormat("yyyy/MM/dd, hh:mm:ss");
		
		try {
			return readableformat.format(ymdhisformat.parse(ymdhis));
		} catch (ParseException e) {
			return ymdhis;
		}
	}
	
	public static String getNowInYmdhis() {
		Date now = new Date();
		return new SimpleDateFormat("yyyyMMddhhmmss").format(now);
	}
}
