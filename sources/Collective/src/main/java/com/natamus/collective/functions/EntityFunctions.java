/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.17.1, mod version: 2.44.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Collective ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.collective.functions;

import java.util.UUID;

import com.natamus.collective.data.GlobalVariables;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityFunctions {
	// START: CHECK functions
	public static Boolean isHorse(Entity entity) {
		if (entity instanceof AbstractHorse) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isModdedVillager(String entitystring) {
		String type = entitystring.split("\\[")[0];
		for (String moddedvillager : GlobalVariables.moddedvillagers) {
			if (type.equalsIgnoreCase(moddedvillager)) {
				return true;
			}
		}
		return false;
	}
	public static boolean isModdedVillager(Entity entity) {
		String entitystring = getEntityString(entity);
		return isModdedVillager(entitystring);
	}
	
	public static boolean isMilkable(Entity entity) {
		if (entity instanceof Sheep || entity instanceof Llama || entity instanceof Pig || entity instanceof Donkey || entity instanceof Horse || entity instanceof Mule) {
			if (entity instanceof Animal == false) {
				return false;
			}
			
			Animal animal = (Animal)entity;
			if (animal.isBaby()) {
				return false;
			}
			return true;
		}
		return false;	
	}
	// END: CHECK functions
	
	
	// START: GET functions
	public static String getEntityString(Entity entity) {
		String entitystring = "";
		try {
			entitystring = entity.getClass().getSimpleName();
		}
		catch (NoClassDefFoundError er) {}
		return entitystring;
	}
	// END: GET functions
	
	
	// Do functions
	public static void nameEntity(Entity entity, String name) {
		if (name != "") {
			entity.setCustomName(new TextComponent(name));
		}
	}
	
	public static void addPotionEffect(Entity entity, MobEffect effect, Integer ms) {
		MobEffectInstance freeze = new MobEffectInstance(effect, ms/50);
		LivingEntity le = (LivingEntity)entity;
		le.addEffect(freeze);
	}
	public static void removePotionEffect(Entity entity, MobEffect effect) {
		LivingEntity le = (LivingEntity)entity;
		le.removeEffect(effect);
	}
	
	public static void chargeEntity(Entity entity) {
		Level world = entity.getCommandSenderWorld();
		Vec3 evec = entity.position();
		
		LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
		lightning.setPos(evec.x(), evec.y(), evec.z());
		lightning.setUUID(new UUID(0, (int)(GlobalVariables.random.nextInt()*1000000)));
		entity.thunderHit((ServerLevel)world, lightning);
		
		entity.clearFire();
	}

	public static void transferItemsBetweenEntities(Entity from, Entity to, boolean ignoremainhand) {
		if (from instanceof Mob == false) {
			return;
		}
		Mob mobfrom = (Mob)from;
		
		for(EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
			if (ignoremainhand) {
				if (equipmentslottype.equals(EquipmentSlot.MAINHAND)) {
					continue;
				}
			}
			
			ItemStack itemstack = mobfrom.getItemBySlot(equipmentslottype);
			if (!itemstack.isEmpty()) {
				to.setItemSlot(equipmentslottype, itemstack.copy());
			}
		}
	}
	public static void transferItemsBetweenEntities(Entity from, Entity to) {
		transferItemsBetweenEntities(from, to, false);
	}
	
	public static Boolean doesEntitySurviveThisDamage(Player player, int halfheartdamage) {
		return doesEntitySurviveThisDamage((LivingEntity)player, halfheartdamage);
	}	
	public static Boolean doesEntitySurviveThisDamage(LivingEntity entity, int halfheartdamage) {
		float newhealth = entity.getHealth() - (float)halfheartdamage;
		if (newhealth > 0f) {
			entity.hurt(DamageSource.MAGIC, 0.1F);
			entity.setHealth(newhealth);
		}
		else {
			entity.hurt(DamageSource.MAGIC, Float.MAX_VALUE);
			return false;
		}
		return true;
	}
}
