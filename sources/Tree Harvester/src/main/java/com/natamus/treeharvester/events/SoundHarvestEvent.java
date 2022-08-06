/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.19.2, mod version: 5.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.treeharvester.events;

import java.util.Date;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class SoundHarvestEvent {
	public static Date lastplayedlog = null;
	public static Date lastplayedleaf = null;
	
	@SubscribeEvent
	public void onSoundEvent(PlaySoundEvent e) {
		String name = e.getName().trim();
		
		if (name.equals("block.grass.break") || name.equals("block.wood.break")) {
			Date now = new Date();
			Date then;
			
			if (name.equals("block.grass.break")) {
				then = lastplayedleaf;
				lastplayedleaf = now;
			}
			else {
				then = lastplayedlog;
				lastplayedlog = now;
			}
			
			if (then != null) {
				long ms = (now.getTime()-then.getTime());
				if (ms < 10) {
					e.setSound(null);
				}
			}
		}
	}
}
