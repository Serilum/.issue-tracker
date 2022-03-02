/*
 * This is the latest source code of Superflat World No Slimes.
 * Minecraft version: 1.19.x, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Superflat World No Slimes ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.superflatworldnoslimes.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class SlimeEvent {
	public static boolean onWorldJoin(Level world, Entity entity) {
		if (world.isClientSide) {
			return true;
		}
		
		ServerLevel serverworld = (ServerLevel)world;
		
		if (entity instanceof Slime) {
			if (serverworld.getServer().getWorldData().worldGenSettings().isFlatWorld()) {
				return false;
			}
		}
		
		return true;
	}
}
