/*
 * This is the latest source code of Configurable Mob Potion Effects.
 * Minecraft version: 1.19.3, mod version: 2.2.
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

package com.natamus.configurablemobpotioneffects.events;

import java.util.concurrent.CopyOnWriteArrayList;

import com.natamus.configurablemobpotioneffects.util.Util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class MobEffectsEvent {
	public static void onEntityJoin(Level world, Entity entity) {
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof LivingEntity == false) {
			return;
		}
		
		EntityType<?> entitytype = entity.getType();
		if (!Util.mobpermanent.containsKey(entitytype)) {
			return;
		}
		
		CopyOnWriteArrayList<MobEffectInstance> effectinstances = Util.mobpermanent.get(entitytype);
		if (effectinstances.size() > 0) {
			LivingEntity le = (LivingEntity)entity;
			
			for (MobEffectInstance effectinstance : effectinstances) {
				MobEffectInstance ei = new MobEffectInstance(effectinstance);
				
				le.removeEffect(ei.getEffect());
				le.addEffect(ei);
			}
		}
	}
	
	public static void onEntityDamage(Level world, Entity entity, DamageSource damageSource, float damageAmount) {
		if (world.isClientSide) {
			return;
		}
		
		Entity source = damageSource.getEntity();
		if (source == null) {
			return;
		}
		
		EntityType<?> sourcetype = source.getType();
		if (!Util.mobdamage.containsKey(sourcetype)) {
			return;
		}
		
		LivingEntity le = (LivingEntity)entity;
		
		CopyOnWriteArrayList<MobEffectInstance> effectinstances = Util.mobdamage.get(sourcetype);
		if (effectinstances.size() > 0) {
			for (MobEffectInstance effectinstance : effectinstances) {
				MobEffectInstance ei = new MobEffectInstance(effectinstance);
				
				le.removeEffect(ei.getEffect());
				le.addEffect(ei);
			}
		}		
	}
}
