/*
 * This is the latest source code of Name Tag Tweaks.
 * Minecraft version: 1.16.5, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Name Tag Tweaks ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.nametagtweaks.events;

import com.natamus.nametagtweaks.config.ConfigHandler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class NameTagEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDeathEvent e) {
		LivingEntity entity = e.getEntityLiving();
		World world = entity.getEntityWorld();
		if (world.isRemote) {
			return;
		}
		
		if (entity.hasCustomName()) {
			if (entity instanceof SlimeEntity) {
				SlimeEntity slime = (SlimeEntity)entity;
				int slimesize = slime.getSlimeSize();
				if (slimesize != 4) {
					return;
				}
			}
			
			ItemStack nametagstack = new ItemStack(Items.NAME_TAG, 1);
			if (ConfigHandler.GENERAL.droppedNameTagbyEntityKeepsNameValue.get()) {
				ITextComponent name = entity.getName();
				nametagstack.setDisplayName(name);
			}
			nametagstack.setRepairCost(0);
			
			Vector3d evec = entity.getPositionVec();
			ItemEntity ie = new ItemEntity(world, evec.getX(), evec.getY()+1, evec.getZ(), nametagstack);
			world.addEntity(ie);
		}
	}
}
