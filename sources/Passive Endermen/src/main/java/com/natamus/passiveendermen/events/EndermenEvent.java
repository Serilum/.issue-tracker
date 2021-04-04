/*
 * This is the latest source code of Passive Endermen.
 * Minecraft version: 1.16.5, mod version: 1.4.
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

import java.lang.reflect.Field;
import java.util.Set;

import com.natamus.passiveendermen.config.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@EventBusSubscriber
public class EndermenEvent {
	private static final Field goals = ObfuscationReflectionHelper.findField(GoalSelector.class, "field_220892_d");
	
	@SubscribeEvent
	public void onEndermanGriefing(EntityMobGriefingEvent e) {
		Entity entity = e.getEntity();
		if (entity == null) {
			return;
		}
		
		World world = entity.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		if (entity instanceof EndermanEntity == false) {
			return;
		}
		
		if (ConfigHandler.GENERAL.preventEndermenFromGriefing.get()) {
			e.setResult(Result.DENY);
		}
	}
	
	@SubscribeEvent
	public void onEndermanTeleport(EnderTeleportEvent e) {
		Entity entity = e.getEntity();
		if (entity == null) {
			return;
		}
		
		World world = entity.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		if (entity instanceof EndermanEntity == false) {
			return;
		}
		
		if (ConfigHandler.GENERAL.preventEndermenFromTeleporting.get()) {
			e.setCanceled(true);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void entitySpawned(EntityJoinWorldEvent e) {
		Entity entity = e.getEntity();
		if (entity == null) {
			return;
		}
		
		World world = entity.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		if (entity instanceof EndermanEntity == false) {
			return;
		}
		
		if (ConfigHandler.GENERAL.preventEndermenFromAttackingFirst.get()) {
			EndermanEntity enderman = (EndermanEntity)entity;
			
			try {
				Set<PrioritizedGoal> set = (Set<PrioritizedGoal>) goals.get(enderman.targetSelector);
				for (PrioritizedGoal goal : set) {
					enderman.targetSelector.removeGoal(goal.getGoal());
				}
			} catch (Exception ex) { }
		}
	}
}
