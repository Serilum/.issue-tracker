/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.18.x, mod version: 2.1.
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

import com.natamus.cyclepaintings.config.ConfigHandler;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Motive;

public class Util {
	private static List<Motive> paintingtypes = new ArrayList<Motive>();
	
	public static void setPaintings() {
		String[] allignore = ConfigHandler.ignorePaintingsInCycleResourceLocation.getValue().split(",");
		boolean debug = ConfigHandler.showRegisteredPaintingsDebug.getValue();
		
		if (debug) {
			System.out.println("[Cycle Paintings Debug] The config option 'showRegisteredPaintingsDebug' has been enabled. Showing paintings during cycle registration.");
		}
		
		for (ResourceLocation motiverl : Registry.MOTIVE.keySet()) {
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
				
				Motive motive = Registry.MOTIVE.get(motiverl);
				paintingtypes.add(motive);
			}
		}
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
