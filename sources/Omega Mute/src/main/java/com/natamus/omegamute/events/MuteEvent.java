/*
 * This is the latest source code of Omega Mute.
 * Minecraft version: 1.18.1, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Omega Mute ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.omegamute.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.omegamute.util.Util;
import com.natamus.omegamute.util.Variables;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class MuteEvent {
	public static List<Player> listeningplayers = new ArrayList<Player>();
	public static HashMap<String, Integer> ismutedsoundmap = new HashMap<String, Integer>();
	public static HashMap<String, Date> lastplayedsound = new HashMap<String, Date>();
	
	@SubscribeEvent
	public void onSoundEvent(PlaySoundEvent e) {	
		String name = e.getName().trim();
		if (ismutedsoundmap.containsKey(name)) {
			int mutedvalue = ismutedsoundmap.get(name);
			if (mutedvalue >= 0) {
				if (mutedvalue == 0) {
					e.setSound(null);
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
							e.setSound(null);
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
			for (Player player : listeningplayers) {
				StringFunctions.sendMessage(player, name, ChatFormatting.WHITE);
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
		
		if (e.getKey() == Variables.hotkey.getKey().getValue()) {
			try {
				Util.loadSoundFile();
			} catch (Exception ex) { return; }
			
			if (mc.player != null) {
				StringFunctions.sendMessage(mc.player, "Reloading the omega mute soundmap file now.", ChatFormatting.DARK_GREEN);
				try {
					if (Util.loadSoundFile()) {
						StringFunctions.sendMessage(mc.player, "New soundmap changes successfully loaded.", ChatFormatting.DARK_GREEN);
					}
					else {
						StringFunctions.sendMessage(mc.player, "No soundmap found, a new one has been generated.", ChatFormatting.DARK_GREEN);
					}
				} catch (Exception ex) {
					StringFunctions.sendMessage(mc.player, "Something went wrong while loading the soundmap file.", ChatFormatting.RED);
				}	
			}
		}
	}
}