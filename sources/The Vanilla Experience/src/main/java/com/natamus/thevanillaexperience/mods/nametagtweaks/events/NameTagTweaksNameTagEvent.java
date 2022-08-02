/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.nametagtweaks.events;

import com.natamus.thevanillaexperience.mods.nametagtweaks.config.NameTagTweaksConfigHandler;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class NameTagTweaksNameTagEvent {
	@SubscribeEvent
	public void mobItemDrop(LivingDeathEvent e) {
		LivingEntity entity = e.getEntityLiving();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity.hasCustomName()) {
			if (entity instanceof Slime) {
				Slime slime = (Slime)entity;
				int slimesize = slime.getSize();
				if (slimesize != 4) {
					return;
				}
			}
			
			ItemStack nametagstack = new ItemStack(Items.NAME_TAG, 1);
			if (NameTagTweaksConfigHandler.GENERAL.droppedNameTagbyEntityKeepsNameValue.get()) {
				Component name = entity.getName();
				nametagstack.setHoverName(name);
			}
			nametagstack.setRepairCost(0);
			
			Vec3 evec = entity.position();
			ItemEntity ie = new ItemEntity(world, evec.x(), evec.y()+1, evec.z(), nametagstack);
			world.addFreshEntity(ie);
		}
	}
}
