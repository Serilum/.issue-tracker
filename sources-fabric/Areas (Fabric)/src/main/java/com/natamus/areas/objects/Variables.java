/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.2, mod version: 3.1.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.areas.objects;

import com.natamus.areas.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Variables {
	public static ResourceLocation networkchannel = new ResourceLocation(Reference.MOD_ID, Reference.MOD_ID);
	public static HashMap<Level, HashMap<BlockPos, AreaObject>> areasperworld = new HashMap<Level, HashMap<BlockPos, AreaObject>>();
	public static HashMap<Level, CopyOnWriteArrayList<BlockPos>> ignoresignsperworld = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();
	
	public static HashMap<Level, CopyOnWriteArrayList<BlockPos>> checkifshouldignoreperworld = new HashMap<Level, CopyOnWriteArrayList<BlockPos>>();
	public static HashMap<Level, HashMap<BlockPos, Integer>> ignoremap = new HashMap<Level, HashMap<BlockPos, Integer>>();
}
