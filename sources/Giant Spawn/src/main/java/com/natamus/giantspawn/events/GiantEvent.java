/*
 * This is the latest source code of Giant Spawn.
 * Minecraft version: 1.17.1, mod version: 2.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Giant Spawn ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.giantspawn.events;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.giantspawn.ai.GiantAttackGoal;
import com.natamus.giantspawn.ai.GiantAttackTurtleEggGoal;
import com.natamus.giantspawn.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GiantEvent {
	private static HashMap<Level, CopyOnWriteArrayList<Entity>> giants_per_world = new HashMap<Level, CopyOnWriteArrayList<Entity>>();
	private static HashMap<Level, Integer> tickdelay_per_world = new HashMap<Level, Integer>();
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Giant == false) {
			return;
		}
		
		if (!giants_per_world.get(world).contains(entity)) {
			giants_per_world.get(world).add(entity);
		}

		Giant giant = (Giant)entity;
		
		giant.goalSelector.addGoal(4, new GiantAttackTurtleEggGoal(giant, 2.0D, 3));
		giant.goalSelector.addGoal(8, new LookAtPlayerGoal(giant, Player.class, 8.0F));
		giant.goalSelector.addGoal(8, new RandomLookAroundGoal(giant));
	      
		giant.goalSelector.addGoal(2, new GiantAttackGoal(giant, 2.0D, false));
		giant.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(giant, 2.0D));
		giant.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(giant, Player.class, true));
		giant.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(giant, AbstractVillager.class, false));
		giant.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(giant, IronGolem.class, true));
		giant.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(giant, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));	   
		
		giant.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(35.0D); // FOLLOW_RANGE
		giant.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.23F * ConfigHandler.GENERAL.giantMovementSpeedModifier.get()); // MOVEMENT_SPEED
		giant.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D * ConfigHandler.GENERAL.giantAttackDamageModifier.get()); // ATTACK_DAMAGE
		giant.getAttribute(Attributes.ARMOR).setBaseValue(2.0D); // ARMOR
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		Level world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		int ticks = tickdelay_per_world.get(world);
		if (ticks % 20 != 0) {
			tickdelay_per_world.put(world, ticks + 1);
			return;
		}
		tickdelay_per_world.put(world, 1);
		
		if (!ConfigHandler.GENERAL.shouldBurnGiantsInDaylight.get()) {
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
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		giants_per_world.put(world, new CopyOnWriteArrayList<Entity>());
		tickdelay_per_world.put(world, 1);
	}
}
