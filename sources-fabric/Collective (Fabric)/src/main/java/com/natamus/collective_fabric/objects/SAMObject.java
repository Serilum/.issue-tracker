/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.18.x, mod version: 4.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
