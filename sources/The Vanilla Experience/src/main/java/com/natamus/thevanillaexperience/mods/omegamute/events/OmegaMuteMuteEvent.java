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

package com.natamus.thevanillaexperience.mods.omegamute.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.omegamute.util.OmegaMuteUtil;
import com.natamus.thevanillaexperience.mods.omegamute.util.OmegaMuteVariables;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class OmegaMuteMuteEvent {
	public static List<PlayerEntity> listeningplayers = new ArrayList<PlayerEntity>();
	public static HashMap<String, Integer> ismutedsoundmap = new HashMap<String, Integer>();
	public static HashMap<String, Date> lastplayedsound = new HashMap<String, Date>();
	
	@SubscribeEvent
	public void onSoundEvent(PlaySoundEvent e) {	
		String name = e.getName().trim();
		if (ismutedsoundmap.containsKey(name)) {
			int mutedvalue = ismutedsoundmap.get(name);
			if (mutedvalue >= 0) {
				if (mutedvalue == 0) {
					e.setResultSound(null);
					name = "(muted) " + name;
				}
				else {
					Date now = new Date();
					
					boolean replace = true;
					if (lastplayedsound.containsKey(name)) {
						Date then = lastplayedsound.get(name);
						long ms = (now.getTime()-then.getTime());
						if (ms < mutedvalue*1000) {
							replace = false;
							e.setResultSound(null);
							name = "(" + mutedvalue + "-culled-muted) " + name;
						}
					}
					
					if (replace) {
						lastplayedsound.put(name, now);
						name = "(" + mutedvalue + "-culled-allowed) " + name;
					}
				}
			}
		}
		
		if (listeningplayers.size() > 0) {
			for (PlayerEntity player : listeningplayers) {
				StringFunctions.sendMessage(player, name, TextFormatting.WHITE);
			}
		}
	}
	
	private static Minecraft mc = null;
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent e) {
		if (e.getAction() != 1) {
			return;
		}
		
		if (mc == null) {
			mc = Minecraft.getInstance();
		}
		
		if (mc.screen instanceof ChatScreen) {
			return;
		}
		
		if (e.getKey() == OmegaMuteVariables.hotkey.getKey().getValue()) {
			try {
				OmegaMuteUtil.loadSoundFile();
			} catch (Exception ex) { return; }
			
			if (mc.player != null) {
				StringFunctions.sendMessage(mc.player, "Reloading the omega mute soundmap file now.", TextFormatting.DARK_GREEN);
				try {
					if (OmegaMuteUtil.loadSoundFile()) {
						StringFunctions.sendMessage(mc.player, "New soundmap changes successfully loaded.", TextFormatting.DARK_GREEN);
					}
					else {
						StringFunctions.sendMessage(mc.player, "No soundmap found, a new one has been generated.", TextFormatting.DARK_GREEN);
					}
				} catch (Exception ex) {
					StringFunctions.sendMessage(mc.player, "Something went wrong while loading the soundmap file.", TextFormatting.RED);
				}	
			}
		}
	}
}