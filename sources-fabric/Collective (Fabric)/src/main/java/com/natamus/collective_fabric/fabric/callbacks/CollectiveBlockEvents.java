/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.41.
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

package com.natamus.collective_fabric.fabric.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalShape;

public class CollectiveBlockEvents {
	private CollectiveBlockEvents() { }
	 
    public static final Event<CollectiveBlockEvents.Possible_Portal_Spawn> ON_NETHER_PORTAL_SPAWN = EventFactory.createArrayBacked(CollectiveBlockEvents.Possible_Portal_Spawn.class, callbacks -> (world, pos, shape) -> {
        for (CollectiveBlockEvents.Possible_Portal_Spawn callback : callbacks) {
        	callback.onPossiblePortal(world, pos, shape);
        }
    });
    
	@FunctionalInterface
	public interface Possible_Portal_Spawn {
		 void onPossiblePortal(Level world, BlockPos pos, PortalShape shape);
	}
}
