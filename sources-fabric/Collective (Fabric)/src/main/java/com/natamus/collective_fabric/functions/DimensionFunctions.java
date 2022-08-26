/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.50.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective_fabric.functions;

import net.minecraft.world.level.Level;

public class DimensionFunctions {
	public static String getSimpleDimensionString(Level world) {
		String dimensionfolder = WorldFunctions.getWorldDimensionName(world).toLowerCase();
		if (dimensionfolder.contains(":")) {
			dimensionfolder = dimensionfolder.split(":")[1];
		}
		return dimensionfolder;
	}
}
