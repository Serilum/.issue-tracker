/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.advancementscreenshot.events;

import com.natamus.thevanillaexperience.mods.advancementscreenshot.config.AdvancementScreenshotConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AdvancementScreenshotAdvancementGetEvent {
	private boolean takescreenshot = false;
	private int cooldown = -1;
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) {
		if (!e.phase.equals(Phase.END)) {
			return;
		}
		
		if (cooldown > 0) {
			cooldown -= 1;
			return;
		}
		
		if (takescreenshot) {
			if (cooldown < 0) {
				cooldown = 20;
				return;
			}
			
			Minecraft mc = Minecraft.getInstance();
			Screenshot.grab(mc.gameDirectory, mc.getMainRenderTarget(), (context) -> {
				mc.execute(() -> {
					if (AdvancementScreenshotConfigHandler.GENERAL.showScreenshotTakenMessage.get()) {
						mc.gui.getChat().addMessage(context);
					}
				});
			});
			
			takescreenshot = false;
			cooldown = -1;
		}
	}
	
	@SubscribeEvent
	public void onAdvancement(AdvancementEvent e) {
		if (!AdvancementScreenshotConfigHandler.GENERAL.countNewRecipeAdvancements.get()) {
			boolean recipe = false;
			String[][] requirements = e.getAdvancement().getRequirements();
			for (String[] requirement : requirements) {
				for (String req : requirement) {
					if (req.contains("has_the_recipe")) {
						recipe = true;
						break;
					}
				}
				
				if (recipe) {
					break;
				}
			}
		
			if (recipe) {
				return;
			}
		}

		takescreenshot = true;
		cooldown = 20;
	}
}
