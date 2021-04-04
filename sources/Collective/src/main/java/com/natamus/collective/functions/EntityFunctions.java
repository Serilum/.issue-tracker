/*
 * This is the latest source code of Collective.
 * Minecraft version: 1.16.5, mod version: 2.25.
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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.MuleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class EntityFunctions {
	// START: CHECK functions
	public static Boolean isHorse(Entity entity) {
		if (entity instanceof AbstractHorseEntity) {
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
		if (entity instanceof SheepEntity || entity instanceof LlamaEntity || entity instanceof PigEntity || entity instanceof DonkeyEntity || entity instanceof HorseEntity || entity instanceof MuleEntity) {
			if (entity instanceof AnimalEntity == false) {
				return false;
			}
			
			AnimalEntity animal = (AnimalEntity)entity;
			if (animal.isChild()) {
				return false;
			}
			return true;
		}
		return false;	
	}
	// END: CHECK functions
	
	
	// START: GET functions
	public static String getEntityString(Entity entity) {
		try {
			return entity.toString();
		}
		catch (Exception | Error er) {
			return "";
		}
	}
	// END: GET functions
	
	
	// Do functions
	public static void nameEntity(Entity entity, String name) {
		if (name != "") {
			entity.setCustomName(new StringTextComponent(name));
		}
	}
	
	public static void addPotionEffect(Entity entity, Effect effect, Integer ms) {
		EffectInstance freeze = new EffectInstance(effect, ms/50);
		LivingEntity le = (LivingEntity)entity;
		le.addPotionEffect(freeze);
	}
	public static void removePotionEffect(Entity entity, Effect effect) {
		LivingEntity le = (LivingEntity)entity;
		le.removePotionEffect(effect);
	}
	
	public static void chargeEntity(Entity entity) {
		World world = entity.getEntityWorld();
		Vector3d evec = entity.getPositionVec();
		
		LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
		lightning.setPosition(evec.getX(), evec.getY(), evec.getZ());
		lightning.setUniqueId(new UUID(0, (int)(GlobalVariables.random.nextInt()*1000000)));
		entity.func_241841_a((ServerWorld)world, lightning);
		
		entity.extinguish();
	}

	public static void transferItemsBetweenEntities(Entity from, Entity to, boolean ignoremainhand) {
		if (from instanceof MobEntity == false) {
			return;
		}
		MobEntity mobfrom = (MobEntity)from;
		
		for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
			if (ignoremainhand) {
				if (equipmentslottype.equals(EquipmentSlotType.MAINHAND)) {
					continue;
				}
			}
			
			ItemStack itemstack = mobfrom.getItemStackFromSlot(equipmentslottype);
			if (!itemstack.isEmpty()) {
				to.setItemStackToSlot(equipmentslottype, itemstack.copy());
			}
		}
	}
	public static void transferItemsBetweenEntities(Entity from, Entity to) {
		transferItemsBetweenEntities(from, to, false);
	}
	
	public static Boolean doesEntitySurviveThisDamage(PlayerEntity player, int halfheartdamage) {
		return doesEntitySurviveThisDamage((LivingEntity)player, halfheartdamage);
	}	
	public static Boolean doesEntitySurviveThisDamage(LivingEntity entity, int halfheartdamage) {
		float newhealth = entity.getHealth() - (float)halfheartdamage;
		if (newhealth > 0f) {
			entity.attackEntityFrom(DamageSource.MAGIC, 0.1F);
			entity.setHealth(newhealth);
		}
		else {
			entity.attackEntityFrom(DamageSource.MAGIC, Float.MAX_VALUE);
			return false;
		}
		return true;
	}
}
