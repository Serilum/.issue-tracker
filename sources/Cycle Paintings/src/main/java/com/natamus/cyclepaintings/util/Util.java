/*
 * This is the latest source code of Cycle Paintings.
 * Minecraft version: 1.19.2, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
