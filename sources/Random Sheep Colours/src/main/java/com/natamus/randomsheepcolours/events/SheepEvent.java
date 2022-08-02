/*
 * This is the latest source code of Random Sheep Colours.
 * Minecraft version: 1.19.1, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.randomsheepcolours.events;

import java.util.Set;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.util.Reference;
import com.natamus.randomsheepcolours.util.Util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SheepEvent {
	@SubscribeEvent
	public void onSheepSpawn(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Sheep == false) {
			return;
		}
		
		String sheeptag = Reference.MOD_ID + ".coloured";
		Set<String> tags = entity.getTags();
		if (tags.contains(sheeptag)) {
			return;
		}
		
		if (Util.possibleColours == null) {
			return;
		}
		if (Util.possibleColours.size() < 1) {
			return;
		}
		
		Sheep sheep = (Sheep)entity;
		
		if (!((AgeableMob)entity).isBaby()) {
			int randomindex = GlobalVariables.random.nextInt(Util.possibleColours.size());
			final DyeColor randomcolour = Util.possibleColours.get(randomindex);
			
			if (!sheep.isAlive()) {
				return;
			}
			
			if (randomcolour == null) {
				sheep.setCustomName(Component.literal("jeb_"));
				sheep.setCustomNameVisible(false);
			}
			else {
				sheep.setColor(randomcolour);
			}
		}
		
		sheep.addTag(sheeptag);
	}
}
