/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.nametagtweaks.events;

import com.natamus.thevanillaexperience.mods.nametagtweaks.config.NameTagTweaksConfigHandler;

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
public class NameTagTweaksNameTagEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDeathEvent e) {
		LivingEntity entity = e.getEntityLiving();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity.hasCustomName()) {
			if (entity instanceof SlimeEntity) {
				SlimeEntity slime = (SlimeEntity)entity;
				int slimesize = slime.getSize();
				if (slimesize != 4) {
					return;
				}
			}
			
			ItemStack nametagstack = new ItemStack(Items.NAME_TAG, 1);
			if (NameTagTweaksConfigHandler.GENERAL.droppedNameTagbyEntityKeepsNameValue.get()) {
				ITextComponent name = entity.getName();
				nametagstack.setHoverName(name);
			}
			nametagstack.setRepairCost(0);
			
			Vector3d evec = entity.position();
			ItemEntity ie = new ItemEntity(world, evec.x(), evec.y()+1, evec.z(), nametagstack);
			world.addFreshEntity(ie);
		}
	}
}
