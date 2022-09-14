/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.randomsheepcolours.events;

import java.util.Set;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.util.Reference;
import com.natamus.thevanillaexperience.mods.randomsheepcolours.util.RandomSheepColoursUtil;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RandomSheepColoursSheepEvent {
	@SubscribeEvent
	public void onSheepSpawn(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
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
		
		if (RandomSheepColoursUtil.possibleColours == null) {
			return;
		}
		if (RandomSheepColoursUtil.possibleColours.size() < 1) {
			return;
		}
		
		Sheep sheep = (Sheep)entity;
		
		if (!((AgeableMob)entity).isBaby()) {
			int randomindex = GlobalVariables.random.nextInt(RandomSheepColoursUtil.possibleColours.size());
			final DyeColor randomcolour = RandomSheepColoursUtil.possibleColours.get(randomindex);
			
			if (!sheep.isAlive()) {
				return;
			}
			
			if (randomcolour == null) {
				sheep.setCustomName(new TextComponent("jeb_"));
				sheep.setCustomNameVisible(false);
			}
			else {
				sheep.setColor(randomcolour);
			}
		}
		
		sheep.addTag(sheeptag);
	}
}
