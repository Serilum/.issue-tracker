/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.snowballsfreezemobs.events;

import java.util.HashMap;
import java.util.Map;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.thevanillaexperience.mods.snowballsfreezemobs.config.SnowballsFreezeMobsConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SnowballsFreezeMobsSnowEvent {
	private static Map<LivingEntity, Vector3d> hitentities = new HashMap<LivingEntity, Vector3d>();
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (e.getSource().getDirectEntity() instanceof SnowballEntity == false) {
			return;
		}
		
		if (entity instanceof PlayerEntity) {
			return;
		}
		
		e.setCanceled(true);
		EntityFunctions.addPotionEffect(entity, Effects.BLINDNESS, SnowballsFreezeMobsConfigHandler.GENERAL.freezeTimeMs.get());
		hitentities.put((LivingEntity)entity, entity.position());
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if(entity instanceof LivingEntity == false) {
			return;
		}
		LivingEntity le = (LivingEntity)entity;
		
		if (le.getEffect(Effects.BLINDNESS) != null) {
			if (hitentities.containsKey(le)) {
				Vector3d lastvec = hitentities.get(le);
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
