/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.cyclepaintings.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.registries.ForgeRegistries;

public class CyclePaintingsUtil {
	private static List<PaintingType> paintingtypes;
	
	public static void setPaintings () {
		paintingtypes = new ArrayList<>(ForgeRegistries.PAINTING_TYPES.getValues());
	}
	
	public static List<PaintingType> getSimilarArt(PaintingType currentart) {
		List<PaintingType> similarart = new ArrayList<PaintingType>();
		int xsize = currentart.getWidth();
		int ysize = currentart.getHeight();
		
		for (PaintingType aa : paintingtypes) {
			if (aa.getWidth() == xsize && aa.getHeight() == ysize) {
				similarart.add(aa);
			}
		}
		
		return similarart;
	}
}
