/*
 * This is the latest source code of Random Mob Effects.
 * Minecraft version: 1.16.5, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Random Mob Effects ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.randommobeffects.events;

import java.util.Set;

import com.natamus.collective.util.Reference;
import com.natamus.randommobeffects.config.ConfigHandler;
import com.natamus.randommobeffects.util.Util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AddEffectEvent {
	@SubscribeEvent
	public void onSheepSpawn(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof LivingEntity == false) {
			return;
		}
		if (!entity.getType().getCategory().equals(EntityClassification.MONSTER)) {
			return;
		}
		
		String effecttag = Reference.MOD_ID + ".effectadded";
		Set<String> tags = entity.getTags();
		if (tags.contains(effecttag)) {
			return;
		}
		
		LivingEntity le = (LivingEntity)entity;
		Effect randomeffect = Util.getRandomEffect();
		
		EffectInstance effectinstance;
		if (ConfigHandler.GENERAL.hideEffectParticles.get()) {
			effectinstance = new EffectInstance(randomeffect, Integer.MAX_VALUE, ConfigHandler.GENERAL.potionEffectLevel.get()-1, true, false);
		}
		else {
			effectinstance = new EffectInstance(randomeffect, Integer.MAX_VALUE, ConfigHandler.GENERAL.potionEffectLevel.get()-1);
		}
		
		le.addEffect(effectinstance);
		
		entity.addTag(effecttag);
	}
}
