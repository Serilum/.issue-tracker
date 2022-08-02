/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.areas.objects;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class AreasVariables {
	public static HashMap<Level, HashMap<BlockPos, AreaObject>> areasperworld = new HashMap<Level, HashMap<BlockPos, AreaObject>>();
	public static HashMap<Level, CopyOnWriteArrayList<BlockPos>> ignoresignsperworld = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();
	
	public static HashMap<Level, CopyOnWriteArrayList<BlockPos>> checkifshouldignoreperworld = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();
	public static HashMap<Level, HashMap<BlockPos, Integer>> ignoremap = new HashMap<Level, HashMap<BlockPos, Integer>>();
}
