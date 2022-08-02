/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.passiveshield.events;

import com.natamus.thevanillaexperience.mods.passiveshield.config.PassiveShieldConfigHandler;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.RawMouseEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PassiveShieldClientEvent {
	private static int ignorecount = 0;
	private static boolean usingshield = false;
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onHandRender(RenderHandEvent e) {
		InteractionHand hand = e.getHand();
		ItemStack itemstack = e.getItemStack();
		
		if (hand.equals(InteractionHand.OFF_HAND)) {
			if (PassiveShieldConfigHandler.GENERAL.hideShieldWhenNotInUse.get()) {
				if (itemstack.getItem() instanceof ShieldItem) {
					if (!usingshield) {
						e.setCanceled(true);
					}
				}
			}
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onItemUse(LivingEntityUseItemEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (!world.isClientSide) {
			return;
		}
		
		ItemStack usestack = e.getItem();
		if (usestack.getItem() instanceof ShieldItem) {
			if (ignorecount > 0) {
				ignorecount -= 1;
				return;
			}
			usingshield = true;
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onMouseRelease(RawMouseEvent e) {
		if (usingshield) {
			int button = e.getButton();
			if (button == 1) {
				int action = e.getAction();
				if (action == 0) {
					ignorecount = 3;
					usingshield = false;
				}
			}
		}
	}
}
