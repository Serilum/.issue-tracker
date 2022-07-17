/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.19.0, mod version: 2.4.
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

import com.natamus.cyclepaintings.config.ConfigHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class Util {
	private static List<PaintingVariant> paintingtypes = new ArrayList<PaintingVariant>();
	
	public static void setPaintings() {
		String[] allignore = ConfigHandler.GENERAL.ignorePaintingsInCycleResourceLocation.get().split(",");
		boolean debug = ConfigHandler.GENERAL.showRegisteredPaintingsDebug.get();
		
		if (debug) {
			System.out.println("[Cycle Paintings Debug] The config option 'showRegisteredPaintingsDebug' has been enabled. Showing paintings during cycle registration.");
		}
		
		for (ResourceLocation motiverl : ForgeRegistries.PAINTING_VARIANTS.getKeys()) {
			if (motiverl == null) {
				continue;
			}
			
			boolean allowed = true;
			String motiverls = motiverl.toString().toLowerCase();
			for (String toignore : allignore) {
				toignore = toignore.toLowerCase().trim();
				if (toignore.contains(":")) {
					if (motiverls.equals(toignore)) {
						allowed = false;
						break;
					}
				}
				else if (motiverls.split(":")[0].contains(toignore)) {
					allowed = false;
					break;
				}
			}
			
			if (!allowed) {
				if (debug) {
					System.out.println("[Cycle Paintings Debug] " + motiverls + " (ignored)");
				}
			}
			else {
				if (debug) {
					System.out.println("[Cycle Paintings Debug] " + motiverls + " (allowed)");
				}
				
				PaintingVariant motive = ForgeRegistries.PAINTING_VARIANTS.getValue(motiverl);
				paintingtypes.add(motive);
			}
		}
	}
	
	public static List<PaintingVariant> getSimilarArt(PaintingVariant currentart) {
		List<PaintingVariant> similarart = new ArrayList<PaintingVariant>();
		int xsize = currentart.getWidth();
		int ysize = currentart.getHeight();
		
		for (PaintingVariant aa : paintingtypes) {
			if (aa.getWidth() == xsize && aa.getHeight() == ysize) {
				similarart.add(aa);
			}
		}
		
		return similarart;
	}
}
