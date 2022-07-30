/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.36.
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

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
    
    public static final Event<CollectivePlayerEvents.Player_Dig_Speed_Calc> ON_PLAYER_DIG_SPEED_CALC = EventFactory.createArrayBacked(CollectivePlayerEvents.Player_Dig_Speed_Calc.class, callbacks -> (world, player, digSpeed, state) -> {
        for (CollectivePlayerEvents.Player_Dig_Speed_Calc callback : callbacks) {
        	float newSpeed = callback.onDigSpeedCalc(world, player, digSpeed, state);
        	if (newSpeed != digSpeed) {
        		return newSpeed;
        	}
        }
        
        return -1;
    });
    
    public static final Event<CollectivePlayerEvents.Player_Logged_In> PLAYER_LOGGED_IN = EventFactory.createArrayBacked(CollectivePlayerEvents.Player_Logged_In.class, callbacks -> (world, player) -> {
        for (CollectivePlayerEvents.Player_Logged_In callback : callbacks) {
        	callback.onPlayerLoggedIn(world, player);
        }
    });
    
    public static final Event<CollectivePlayerEvents.Player_Logged_Out> PLAYER_LOGGED_OUT = EventFactory.createArrayBacked(CollectivePlayerEvents.Player_Logged_Out.class, callbacks -> (world, player) -> {
        for (CollectivePlayerEvents.Player_Logged_Out callback : callbacks) {
        	callback.onPlayerLoggedOut(world, player);
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
	
	@FunctionalInterface
	public interface Player_Dig_Speed_Calc {
		 float onDigSpeedCalc(Level world, Player player, float digSpeed, BlockState state);
	}
	
	@FunctionalInterface
	public interface Player_Logged_In {
		 void onPlayerLoggedIn(Level world, Player player);
	}
	
	@FunctionalInterface
	public interface Player_Logged_Out {
		 void onPlayerLoggedOut(Level world, Player player);
	}
}
