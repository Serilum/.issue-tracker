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

package com.natamus.thevanillaexperience.mods.alwaysawitherskull.events;

import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AlwaysaWitherSkullEntityEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDropsEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		if (entity instanceof WitherSkeletonEntity == false) {
			return;
		}
		
		Boolean foundskull = false;
		
		Collection<ItemEntity> drops = e.getDrops();
		for (ItemEntity eitem : drops) {
			if (eitem.getItem().getItem().equals(Items.WITHER_SKELETON_SKULL)) {
				foundskull = true;
				break;
			}
		}
		
		if (!foundskull) {
			ItemEntity newskull = new ItemEntity(entity.world, entity.getPosX(), entity.getPosY()+1, entity.getPosZ(), new ItemStack(Items.WITHER_SKELETON_SKULL, 1));
			drops.add(newskull);
		}
	}
}
