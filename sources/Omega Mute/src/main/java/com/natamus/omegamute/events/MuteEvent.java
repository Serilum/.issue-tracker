/*
 * This is the latest source code of Omega Mute.
 * Minecraft version: 1.19.1, mod version: 2.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
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
	public void onKey(InputEvent.Key e) {
		if (e.getAction() != 1) {
			return;
		}
		
		if (mc == null) {
			mc = Minecraft.getInstance();
		}
		
		if (mc.screen instanceof ChatScreen) {
			return;
		}
		
		if (Variables.hotkey != null && e.getKey() == Variables.hotkey.getKey().getValue()) {
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