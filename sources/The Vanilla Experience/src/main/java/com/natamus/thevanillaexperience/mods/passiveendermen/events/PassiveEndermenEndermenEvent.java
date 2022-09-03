/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.passiveendermen.events;

import java.util.Iterator;

import com.natamus.thevanillaexperience.mods.passiveendermen.config.PassiveEndermenConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PassiveEndermenEndermenEvent {
	@SubscribeEvent
	public void onEndermanGriefing(EntityMobGriefingEvent e) {
		Entity entity = e.getEntity();
		if (entity == null) {
			return;
		}
		
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof EnderMan == false) {
			return;
		}
		
		if (PassiveEndermenConfigHandler.GENERAL.preventEndermenFromGriefing.get()) {
			e.setResult(Result.DENY);
		}
	}
	
	@SubscribeEvent
	public void onEndermanTeleport(EntityTeleportEvent.EnderEntity e) {
		Entity entity = e.getEntity();
		if (entity == null) {
			return;
		}
		
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof EnderMan == false) {
			return;
		}
		
		if (PassiveEndermenConfigHandler.GENERAL.preventEndermenFromTeleporting.get()) {
			e.setCanceled(true);
		}
		
	}
	
	@SubscribeEvent
	public static void entitySpawned(EntityJoinWorldEvent e) {
		Entity entity = e.getEntity();
		if (entity == null) {
			return;
		}
		
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof EnderMan == false) {
			return;
		}
		
		if (PassiveEndermenConfigHandler.GENERAL.preventEndermenFromAttackingFirst.get()) {
			EnderMan enderman = (EnderMan)entity;
			
			Iterator<WrappedGoal> targetset = enderman.targetSelector.getRunningGoals().iterator();
			while (targetset.hasNext()) {
				WrappedGoal goal = targetset.next();
				enderman.targetSelector.removeGoal(goal.getGoal());
			}
		}
	}
}
