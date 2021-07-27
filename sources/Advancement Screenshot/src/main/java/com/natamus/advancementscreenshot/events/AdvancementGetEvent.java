/*
 * This is the latest source code of Advancement Screenshot.
 * Minecraft version: 1.17.1, mod version: 1.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Advancement Screenshot ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.advancementscreenshot.events;

import com.natamus.advancementscreenshot.config.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AdvancementGetEvent {
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
					if (ConfigHandler.GENERAL.showScreenshotTakenMessage.get()) {
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
		if (!ConfigHandler.GENERAL.countNewRecipeAdvancements.get()) {
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
