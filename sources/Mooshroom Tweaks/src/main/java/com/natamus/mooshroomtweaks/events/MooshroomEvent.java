/*
 * This is the latest source code of Mooshroom Tweaks.
 * Minecraft version: 1.19.2, mod version: 1.9.
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

package com.natamus.mooshroomtweaks.events;

import java.util.Set;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.util.Reference;
import com.natamus.mooshroomtweaks.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MooshroomEvent {
	@SubscribeEvent
	public void onEntityJoin(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide()) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof MushroomCow == false) {
			return;
		}
		
		Set<String> tags = entity.getTags();
		if (tags.contains(Reference.MOD_ID + ".checked")) {
			return;
		}
		entity.addTag(Reference.MOD_ID + ".checked");
		
		double num = Math.random();
		if (num < ConfigHandler.GENERAL.becomeBrownChance.get()) {
			EntityFunctions.chargeEntity(entity);
		}	
	}
}
