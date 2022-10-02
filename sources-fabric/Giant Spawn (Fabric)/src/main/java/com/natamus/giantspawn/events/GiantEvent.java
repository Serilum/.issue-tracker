/*
 * This is the latest source code of Giant Spawn.
 * Minecraft version: 1.19.2, mod version: 3.3.
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

package com.natamus.giantspawn.events;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective_fabric.functions.BlockPosFunctions;
import com.natamus.giantspawn.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.level.Level;

public class GiantEvent {
	private static HashMap<Level, CopyOnWriteArrayList<Entity>> giants_per_world = new HashMap<Level, CopyOnWriteArrayList<Entity>>();
	private static HashMap<Level, Integer> tickdelay_per_world = new HashMap<Level, Integer>();
	
	public static void onEntityJoin(Level world, Entity entity) {
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof Giant == false) {
			return;
		}
		
		if (!giants_per_world.get(world).contains(entity)) {
			giants_per_world.get(world).add(entity);
		}

		Giant giant = (Giant)entity;
		
		giant.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(35.0D); // FOLLOW_RANGE
		giant.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.23F * ConfigHandler.giantMovementSpeedModifier); // MOVEMENT_SPEED
		giant.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D * ConfigHandler.giantAttackDamageModifier); // ATTACK_DAMAGE
		giant.getAttribute(Attributes.ARMOR).setBaseValue(2.0D); // ARMOR
	}
	
	public static void onWorldTick(ServerLevel world) {
		int ticks = tickdelay_per_world.get(world);
		if (ticks % 20 != 0) {
			tickdelay_per_world.put(world, ticks + 1);
			return;
		}
		tickdelay_per_world.put(world, 1);
		
		if (!ConfigHandler.shouldBurnGiantsInDaylight) {
			return;
		}
		
		if (!world.isDay()) {
			return;
		}
		
		for (Entity giant : giants_per_world.get(world)) {
			if (giant.isAlive()) {
				if (!giant.isInWaterRainOrBubble()) {
					BlockPos epos = giant.blockPosition();
					if (BlockPosFunctions.isOnSurface(world, epos)) {
						giant.setSecondsOnFire(3);
					}
				}	
			}
			else {
				giants_per_world.get(world).remove(giant);
			}		
		}
	}
	
	public static void onWorldLoad(ServerLevel world) {
		giants_per_world.put(world, new CopyOnWriteArrayList<Entity>());
		tickdelay_per_world.put(world, 1);
	}
}
