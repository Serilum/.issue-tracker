/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.areas.objects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class AreaObject {
	public Level world;
	public BlockPos location;
	public String areaname;
	public int radius;
	public String customrgb;
	
	public List<Player> containsplayers;
	
	public AreaObject(Level w, BlockPos l, String a, int r, String rgb) {
		world = w;
		location = l;
		areaname = a;
		radius = r;
		customrgb = rgb;
		containsplayers = new ArrayList<Player>();
		
		if (AreasVariables.areasperworld.containsKey(world)) {
			AreasVariables.areasperworld.get(world).put(l, this);
		}
	}
}
