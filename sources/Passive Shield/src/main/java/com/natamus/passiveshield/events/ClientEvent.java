/*
 * This is the latest source code of Passive Shield.
 * Minecraft version: 1.16.5, mod version: 1.3.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Passive Shield ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.passiveshield.events;

import com.natamus.passiveshield.config.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent.RawMouseEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ClientEvent {
	private static int ignorecount = 0;
	private static boolean usingshield = false;
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onHandRender(RenderHandEvent e) {
		Hand hand = e.getHand();
		ItemStack itemstack = e.getItemStack();
		
		if (hand.equals(Hand.OFF_HAND)) {
			if (ConfigHandler.GENERAL.hideShieldWhenNotInUse.get()) {
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
		World world = entity.getEntityWorld();
		if (!world.isRemote) {
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
