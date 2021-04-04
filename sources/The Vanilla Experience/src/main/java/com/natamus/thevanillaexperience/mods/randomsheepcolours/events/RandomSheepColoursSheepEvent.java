/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

package com.natamus.thevanillaexperience.mods.randomsheepcolours.events;

import java.util.Set;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.util.Reference;
import com.natamus.thevanillaexperience.mods.randomsheepcolours.util.RandomSheepColoursUtil;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class RandomSheepColoursSheepEvent {
	@SubscribeEvent
	public void onSheepSpawn(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof SheepEntity == false) {
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
		
		SheepEntity sheep = (SheepEntity)entity;
		
		if (!((AgeableEntity)entity).isChild()) {
			int randomindex = GlobalVariables.random.nextInt(RandomSheepColoursUtil.possibleColours.size());
			final DyeColor randomcolour = RandomSheepColoursUtil.possibleColours.get(randomindex);
			
			if (!sheep.isAlive()) {
				return;
			}
			
			if (randomcolour == null) {
				sheep.setCustomName(new StringTextComponent("jeb_"));
				sheep.setCustomNameVisible(false);
			}
			else {
				sheep.setFleeceColor(randomcolour);
			}
		}
		
		sheep.addTag(sheeptag);
	}
}
