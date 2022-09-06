/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 4.52.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.collective_fabric.objects;

import com.natamus.collective_fabric.data.GlobalVariables;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class SAMObject {
	public EntityType<?> fromtype;
	public EntityType<?> totype;
	public Item helditem;
	
	public double chance;
	public boolean spawner;
	public boolean rideable;
	public boolean surface;
	
	public SAMObject(EntityType<?> fromentitytype, EntityType<?> toentitytype, Item itemtohold, double changechance, boolean mustbespawner, boolean ridenotreplace, boolean onlyonsurface) {
		fromtype = fromentitytype;
		totype = toentitytype;
		helditem = itemtohold;
		
		chance = changechance;
		spawner = mustbespawner;
		rideable = ridenotreplace;
		surface = onlyonsurface;
		
		GlobalVariables.samobjects.add(this);
		if (!GlobalVariables.activesams.contains(fromtype)) {
			GlobalVariables.activesams.add(fromtype);
		}
	}
}
