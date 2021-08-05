/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.giantspawn.events;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.collective.functions.BlockPosFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.giantspawn.ai.GiantAttackGoal;
import com.natamus.thevanillaexperience.mods.giantspawn.ai.GiantAttackTurtleEggGoal;
import com.natamus.thevanillaexperience.mods.giantspawn.config.GiantSpawnConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.GiantEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GiantSpawnGiantEvent {
	private static HashMap<World, CopyOnWriteArrayList<Entity>> giants = new HashMap<World, CopyOnWriteArrayList<Entity>>();
	
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof GiantEntity == false) {
			return;
		}
		
		if (!giants.get(world).contains(entity)) {
			giants.get(world).add(entity);
		}

		GiantEntity giant = (GiantEntity)entity;
		
		giant.goalSelector.addGoal(4, new GiantAttackTurtleEggGoal(giant, 2.0D, 3));
		giant.goalSelector.addGoal(8, new LookAtGoal(giant, PlayerEntity.class, 8.0F));
		giant.goalSelector.addGoal(8, new LookRandomlyGoal(giant));
	      
		giant.goalSelector.addGoal(2, new GiantAttackGoal(giant, 2.0D, false));
		giant.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(giant, 2.0D));
		giant.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(giant, PlayerEntity.class, true));
		giant.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(giant, AbstractVillagerEntity.class, false));
		giant.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(giant, IronGolemEntity.class, true));
		giant.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(giant, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_ON_LAND_SELECTOR));	   
		
		giant.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(35.0D); // FOLLOW_RANGE
		giant.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.23F * GiantSpawnConfigHandler.GENERAL.giantMovementSpeedModifier.get()); // MOVEMENT_SPEED
		giant.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(3.0D * GiantSpawnConfigHandler.GENERAL.giantAttackDamageModifier.get()); // ATTACK_DAMAGE
		giant.getAttribute(Attributes.ARMOR).setBaseValue(2.0D); // ARMOR
	}
	
	int currentticks = 1;
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		World world = e.world;
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		
		if (currentticks % 20 != 0) {
			currentticks += 1;
			return;
		}
		currentticks = 1;
		
		if (!GiantSpawnConfigHandler.GENERAL.shouldBurnGiantsInDaylight.get()) {
			return;
		}
		
		if (!world.isDay()) {
			return;
		}
		
		if (!giants.containsKey(world)) {
			giants.put(world, new CopyOnWriteArrayList<Entity>());
		}
		
		for (Entity giant : giants.get(world)) {
			if (giant.isAlive()) {
				if (!giant.isInWaterRainOrBubble()) {
					BlockPos epos = giant.blockPosition();
					if (BlockPosFunctions.isOnSurface(world, epos)) {
						giant.setSecondsOnFire(3);
					}
				}	
			}
			else {
				giants.get(world).remove(giant);
			}		
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		giants.put(world, new CopyOnWriteArrayList<Entity>());
	}
}
