/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.1, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.areas.objects;

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
		
		if (Variables.areasperworld.containsKey(world)) {
			Variables.areasperworld.get(world).put(l, this);
		}
	}
}
