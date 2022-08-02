/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.19.1, mod version: 4.37.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.collective.functions;

import com.natamus.collective.data.GlobalVariables;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class EntityFunctions {
	// START: CHECK functions
	public static Boolean isHorse(Entity entity) {
		return entity instanceof AbstractHorse;
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
			if (!(entity instanceof Animal)) {
				return false;
			}
			
			Animal animal = (Animal)entity;
			return !animal.isBaby();
		}
		return false;	
	}
	// END: CHECK functions
	
	
	// START: GET functions
	public static String getEntityString(Entity entity) {
		String entitystring = "";
		
		ResourceLocation rl = EntityType.getKey(entity.getType());
		if (rl != null) {
			entitystring = rl.toString(); // minecraft:villager, minecraft:wandering_trader
			if (entitystring.contains(":")) {
				entitystring = entitystring.split(":")[1];
			}
			
			entitystring = StringFunctions.capitalizeEveryWord(entitystring.replace("_", " ")).replace(" ", "").replace("Entity", ""); // Villager, WanderingTrader
		}
		
		return entitystring;
	}
	// END: GET functions
	
	
	// Do functions
	public static void nameEntity(Entity entity, String name) {
		if (!name.equals("")) {
			entity.setCustomName(Component.literal(name));
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
		lightning.setUUID(new UUID(0, GlobalVariables.random.nextInt()*1000000));
		entity.thunderHit((ServerLevel)world, lightning);
		
		entity.clearFire();
	}

	public static void transferItemsBetweenEntities(Entity from, Entity to, boolean ignoremainhand) {
		if (!(from instanceof Mob)) {
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
