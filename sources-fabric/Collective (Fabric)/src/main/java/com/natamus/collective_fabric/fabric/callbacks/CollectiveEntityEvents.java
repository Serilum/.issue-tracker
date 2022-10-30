/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.2, mod version: 5.13.
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

package com.natamus.collective_fabric.fabric.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public final class CollectiveEntityEvents {
	private CollectiveEntityEvents() { }

    public static final Event<CollectiveEntityEvents.Living_Tick> LIVING_TICK = EventFactory.createArrayBacked(CollectiveEntityEvents.Living_Tick.class, callbacks -> (world, entity) -> {
        for (CollectiveEntityEvents.Living_Tick callback : callbacks) {
        	callback.onTick(world, entity);
        }
    }); 
	
    public static final Event<CollectiveEntityEvents.Living_Entity_Death> LIVING_ENTITY_DEATH = EventFactory.createArrayBacked(CollectiveEntityEvents.Living_Entity_Death.class, callbacks -> (world, entity, source) -> {
        for (CollectiveEntityEvents.Living_Entity_Death callback : callbacks) {
        	callback.onDeath(world, entity, source);
        }
    }); 
    
    public static final Event<CollectiveEntityEvents.Pre_Entity_Join_World> PRE_ENTITY_JOIN_WORLD = EventFactory.createArrayBacked(CollectiveEntityEvents.Pre_Entity_Join_World.class, callbacks -> (world, entity) -> {
        for (CollectiveEntityEvents.Pre_Entity_Join_World callback : callbacks) {
        	if (!callback.onPreSpawn(world, entity)) {
        		return false;
        	}
        }
		return true;
    });
    
    public static final Event<CollectiveEntityEvents.Entity_Fall_Damage_Calc> ON_FALL_DAMAGE_CALC = EventFactory.createArrayBacked(CollectiveEntityEvents.Entity_Fall_Damage_Calc.class, callbacks -> (world, entity, f, g) -> {
        for (CollectiveEntityEvents.Entity_Fall_Damage_Calc callback : callbacks) {
        	if (callback.onFallDamageCalc(world, entity, f, g) == 0) {
        		return 0;
        	}
        }
		return 1;
    });
    
    public static final Event<CollectiveEntityEvents.Entity_Living_Damage> ON_LIVING_DAMAGE_CALC = EventFactory.createArrayBacked(CollectiveEntityEvents.Entity_Living_Damage.class, callbacks -> (world, entity, damageSource, damageAmount) -> {
        for (CollectiveEntityEvents.Entity_Living_Damage callback : callbacks) {
        	float newDamage = callback.onLivingDamageCalc(world, entity, damageSource, damageAmount);
        	if (newDamage != damageAmount) {
        		return newDamage;
        	}
        }
        
		return -1;
    });
    
    public static final Event<CollectiveEntityEvents.Entity_Living_Attack> ON_LIVING_ATTACK = EventFactory.createArrayBacked(CollectiveEntityEvents.Entity_Living_Attack.class, callbacks -> (world, entity, damageSource, damageAmount) -> {
        for (CollectiveEntityEvents.Entity_Living_Attack callback : callbacks) {
        	if (!callback.onLivingAttack(world, entity, damageSource, damageAmount)) {
        		return false;
        	}
        }
        
		return true;
    });
    
    public static final Event<CollectiveEntityEvents.Entity_Is_Dropping_Loot> ON_ENTITY_IS_DROPPING_LOOT = EventFactory.createArrayBacked(CollectiveEntityEvents.Entity_Is_Dropping_Loot.class, callbacks -> (world, entity, damageSource) -> {
        for (CollectiveEntityEvents.Entity_Is_Dropping_Loot callback : callbacks) {
        	callback.onDroppingLoot(world, entity, damageSource);
        }
    });
    
    public static final Event<CollectiveEntityEvents.Entity_Is_Jumping> ON_ENTITY_IS_JUMPING = EventFactory.createArrayBacked(CollectiveEntityEvents.Entity_Is_Jumping.class, callbacks -> (world, entity) -> {
        for (CollectiveEntityEvents.Entity_Is_Jumping callback : callbacks) {
        	callback.onLivingJump(world, entity);
        }
    });

	public static final Event<CollectiveEntityEvents.Entity_On_Teleport_Command> ON_ENTITY_TELEPORT_COMMAND = EventFactory.createArrayBacked(CollectiveEntityEvents.Entity_On_Teleport_Command.class, callbacks -> (world, entity, targetX, targetY, targetZ) -> {
		for (CollectiveEntityEvents.Entity_On_Teleport_Command callback : callbacks) {
			if (!callback.onTeleportCommand(world, entity, targetX, targetY, targetZ)) {
				return false;
			}
		}
		return true;
	});
    
	@FunctionalInterface
	public interface Living_Tick {
		 void onTick(Level world, Entity entity);
	}
    
	@FunctionalInterface
	public interface Living_Entity_Death {
		 void onDeath(Level world, Entity entity, DamageSource source);
	}
	
	@FunctionalInterface
	public interface Pre_Entity_Join_World {
		 boolean onPreSpawn(Level world, Entity entity);
	}
	
	@FunctionalInterface
	public interface Entity_Fall_Damage_Calc {
		 int onFallDamageCalc(Level world, Entity entity, float f, float g);
	}
	
	@FunctionalInterface
	public interface Entity_Living_Damage {
		 float onLivingDamageCalc(Level world, Entity entity, DamageSource damageSource, float damageAmount);
	}
	
	@FunctionalInterface
	public interface Entity_Living_Attack {
		 boolean onLivingAttack(Level world, Entity entity, DamageSource damageSource, float damageAmount);
	}
	
	@FunctionalInterface
	public interface Entity_Is_Dropping_Loot {
		 void onDroppingLoot(Level world, Entity entity, DamageSource damageSource);
	}
	
	@FunctionalInterface
	public interface Entity_Is_Jumping {
		 void onLivingJump(Level world, Entity entity);
	}

	@FunctionalInterface
	public interface Entity_On_Teleport_Command {
		boolean onTeleportCommand(Level world, Entity entity, double targetX, double targetY, double targetZ);
	}
}
