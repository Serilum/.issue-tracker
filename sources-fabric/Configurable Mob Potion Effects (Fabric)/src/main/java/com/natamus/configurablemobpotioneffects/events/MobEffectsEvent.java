/*
 * This is the latest source code of Configurable Mob Potion Effects.
 * Minecraft version: 1.19.1, mod version: 1.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Configurable Mob Potion Effects ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
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
