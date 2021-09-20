/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.x, mod version: 1.39.
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class CollectivePlayerEvents {
	private CollectivePlayerEvents() { }

    public static final Event<CollectivePlayerEvents.Player_Tick> PLAYER_TICK = EventFactory.createArrayBacked(CollectivePlayerEvents.Player_Tick.class, callbacks -> (world, player) -> {
        for (CollectivePlayerEvents.Player_Tick callback : callbacks) {
        	callback.onTick(world, player);
        }
    });
	
    public static final Event<CollectivePlayerEvents.Player_Death> PLAYER_DEATH = EventFactory.createArrayBacked(CollectivePlayerEvents.Player_Death.class, callbacks -> (world, player) -> {
        for (CollectivePlayerEvents.Player_Death callback : callbacks) {
        	callback.onDeath(world, player);
        }
    });
    
    public static final Event<CollectivePlayerEvents.Player_Change_Dimension> PLAYER_CHANGE_DIMENSION = EventFactory.createArrayBacked(CollectivePlayerEvents.Player_Change_Dimension.class, callbacks -> (world, player) -> {
        for (CollectivePlayerEvents.Player_Change_Dimension callback : callbacks) {
        	callback.onChangeDimension(world, player);
        }
    });
    
	@FunctionalInterface
	public interface Player_Tick {
		 void onTick(ServerLevel world, ServerPlayer player);
	}
    
	@FunctionalInterface
	public interface Player_Death {
		 void onDeath(ServerLevel world, ServerPlayer player);
	}
	
	@FunctionalInterface
	public interface Player_Change_Dimension {
		 void onChangeDimension(ServerLevel world, ServerPlayer player);
	}
}
