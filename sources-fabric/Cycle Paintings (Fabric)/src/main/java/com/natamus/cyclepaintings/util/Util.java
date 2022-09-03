/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.19.2, mod version: 2.6.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.cyclepaintings.util;

import java.util.ArrayList;
import java.util.List;

import com.natamus.cyclepaintings.config.ConfigHandler;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.core.Holder;

public class Util {
	private static List<PaintingVariant> paintingtypes = new ArrayList<PaintingVariant>();
	
	public static void setPaintings() {
		String[] allignore = ConfigHandler.ignorePaintingsInCycleResourceLocation.getValue().split(",");
		boolean debug = ConfigHandler.showRegisteredPaintingsDebug.getValue();
		
		if (debug) {
			System.out.println("[Cycle Paintings Debug] The config option 'showRegisteredPaintingsDebug' has been enabled. Showing paintings during cycle registration.");
		}
		
		for (ResourceLocation motiverl : Registry.PAINTING_VARIANT.keySet()) {
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
				
				PaintingVariant motive = Registry.PAINTING_VARIANT.get(motiverl);
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
