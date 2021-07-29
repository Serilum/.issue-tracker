/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.17.1, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Cycle Paintings ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.cyclepaintings.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.registries.ForgeRegistries;

public class Util {
	private static List<Motive> paintingtypes;
	
	public static void setPaintings () {
		paintingtypes = new ArrayList<>(ForgeRegistries.PAINTING_TYPES.getValues());
	}
	
	public static List<Motive> getSimilarArt(Motive currentart) {
		List<Motive> similarart = new ArrayList<Motive>();
		int xsize = currentart.getWidth();
		int ysize = currentart.getHeight();
		
		for (Motive aa : paintingtypes) {
			if (aa.getWidth() == xsize && aa.getHeight() == ysize) {
				similarart.add(aa);
			}
		}
		
		return similarart;
	}
}
