/*
 * This is the latest source code of Random Mob Effects.
 * Minecraft version: 1.19.2, mod version: 2.2.
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

package com.natamus.randommobeffects.events;

import java.util.Set;

import com.natamus.collective_fabric.util.Reference;
import com.natamus.randommobeffects.config.ConfigHandler;
import com.natamus.randommobeffects.util.Util;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

public class AddEffectEvent {
	public static void onMobSpawn(Level world, Entity entity) {
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof LivingEntity == false) {
			return;
		}
		
		if (!entity.getType().getCategory().equals(MobCategory.MONSTER)) {
			return;
		}
		
		if (ConfigHandler.disableCreepers.getValue()) {
			if (entity instanceof Creeper) {
				return;
			}
		}
		
		String effecttag = Reference.MOD_ID + ".effectadded";
		Set<String> tags = entity.getTags();
		if (tags.contains(effecttag)) {
			return;
		}
		
		LivingEntity le = (LivingEntity)entity;
		MobEffect randomeffect = Util.getRandomEffect();
		
		MobEffectInstance effectinstance;
		if (ConfigHandler.hideEffectParticles.getValue()) {
			effectinstance = new MobEffectInstance(randomeffect, Integer.MAX_VALUE, ConfigHandler.potionEffectLevel.getValue()-1, true, false);
		}
		else {
			effectinstance = new MobEffectInstance(randomeffect, Integer.MAX_VALUE, ConfigHandler.potionEffectLevel.getValue()-1);
		}
		
		le.addEffect(effectinstance);
		
		entity.addTag(effecttag);
	}
}
