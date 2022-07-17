/*
 * This is the latest source code of Snowballs Freeze Mobs.
 * Minecraft version: 1.19.0, mod version: 1.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Snowballs Freeze Mobs ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.snowballsfreezemobs.events;

import java.util.HashMap;
import java.util.Map;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.snowballsfreezemobs.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SnowEvent {
	private static Map<LivingEntity, Vec3> hitentities = new HashMap<LivingEntity, Vec3>();
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (e.getSource().getDirectEntity() instanceof Snowball == false) {
			return;
		}
		
		if (entity instanceof Player) {
			return;
		}
		
		e.setCanceled(true);
		EntityFunctions.addPotionEffect(entity, MobEffects.BLINDNESS, ConfigHandler.GENERAL.freezeTimeMs.get());
		hitentities.put((LivingEntity)entity, entity.position());
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingTickEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if(entity instanceof LivingEntity == false) {
			return;
		}
		LivingEntity le = (LivingEntity)entity;
		
		if (le.getEffect(MobEffects.BLINDNESS) != null) {
			if (hitentities.containsKey(le)) {
				Vec3 lastvec = hitentities.get(le);
				le.teleportTo(lastvec.x, le.getY(), lastvec.z);
			}
		}
		else {
			if (hitentities.containsKey(le)) {
				hitentities.remove(le);
			}
		}
	}
}
