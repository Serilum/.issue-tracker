/*
 * This is the latest source code of Configurable Mob Potion Effects.
 * Minecraft version: 1.16.5, mod version: 1.2.
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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MobEffectsEvent {
	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof LivingEntity == false) {
			return;
		}
		
		EntityType<?> entitytype = entity.getType();
		if (!Util.mobpermanent.containsKey(entitytype)) {
			return;
		}
		
		CopyOnWriteArrayList<EffectInstance> effectinstances = Util.mobpermanent.get(entitytype);
		if (effectinstances.size() > 0) {
			LivingEntity le = (LivingEntity)entity;
			
			for (EffectInstance effectinstance : effectinstances) {
				EffectInstance ei = new EffectInstance(effectinstance);
				
				le.removePotionEffect(ei.getPotion());
				le.addPotionEffect(ei);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityDamage(LivingHurtEvent e) {
		LivingEntity le = e.getEntityLiving();
		World world = le.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		Entity source = e.getSource().getTrueSource();
		if (source == null) {
			return;
		}
		
		EntityType<?> sourcetype = source.getType();
		if (!Util.mobdamage.containsKey(sourcetype)) {
			return;
		}
		
		CopyOnWriteArrayList<EffectInstance> effectinstances = Util.mobdamage.get(sourcetype);
		if (effectinstances.size() > 0) {
			for (EffectInstance effectinstance : effectinstances) {
				EffectInstance ei = new EffectInstance(effectinstance);
				
				le.removePotionEffect(ei.getPotion());
				le.addPotionEffect(ei);
			}
		}		
	}
}
