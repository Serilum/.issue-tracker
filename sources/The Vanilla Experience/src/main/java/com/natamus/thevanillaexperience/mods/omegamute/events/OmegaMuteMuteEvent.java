/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.omegamute.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.omegamute.util.OmegaMuteUtil;
import com.natamus.thevanillaexperience.mods.omegamute.util.OmegaMuteVariables;

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
public class OmegaMuteMuteEvent {
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
		
		if (e.getKey() == OmegaMuteVariables.hotkey.getKey().getValue()) {
			try {
				OmegaMuteUtil.loadSoundFile();
			} catch (Exception ex) { return; }
			
			if (mc.player != null) {
				StringFunctions.sendMessage(mc.player, "Reloading the omega mute soundmap file now.", ChatFormatting.DARK_GREEN);
				try {
					if (OmegaMuteUtil.loadSoundFile()) {
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