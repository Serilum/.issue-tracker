/*
 * This is the latest source code of Passive Endermen.
 * Minecraft version: 1.18.1, mod version: 1.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Passive Endermen ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.passiveendermen.events;

import java.util.Iterator;

import com.natamus.passiveendermen.config.ConfigHandler;

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
public class EndermenEvent {
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
		
		if (ConfigHandler.GENERAL.preventEndermenFromGriefing.get()) {
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
		
		if (ConfigHandler.GENERAL.preventEndermenFromTeleporting.get()) {
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
		
		if (ConfigHandler.GENERAL.preventEndermenFromAttackingFirst.get()) {
			EnderMan enderman = (EnderMan)entity;
			
			Iterator<WrappedGoal> targetset = enderman.targetSelector.getRunningGoals().iterator();
			while (targetset.hasNext()) {
				WrappedGoal goal = targetset.next();
				enderman.targetSelector.removeGoal(goal.getGoal());
			}
		}
	}
}
