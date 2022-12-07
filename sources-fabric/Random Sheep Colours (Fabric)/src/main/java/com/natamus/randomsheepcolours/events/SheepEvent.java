/*
 * This is the latest source code of Random Sheep Colours.
 * Minecraft version: 1.19.3, mod version: 2.4.
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

package com.natamus.randomsheepcolours.events;

import java.util.Set;

import com.natamus.collective_fabric.data.GlobalVariables;
import com.natamus.collective_fabric.util.Reference;
import com.natamus.randomsheepcolours.util.Util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

public class SheepEvent {
	public static void onSheepSpawn(Level world, Entity entity) {
		if (world.isClientSide) {
			return;
		}
		
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
