/*
 * This is the latest source code of Tree Harvester.
 * Minecraft version: 1.19.1, mod version: 5.5.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Tree Harvester ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.treeharvester.events;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

import java.util.Date;

public class SoundHarvestEvent {
	public static Date lastplayedlog = null;
	public static Date lastplayedleaf = null;
	
	public static boolean onSoundEvent(SoundEngine soundEngine, SoundInstance soundInstance) {
		String name = soundInstance.toString();
		
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
					return false;
				}
			}
		}

		return true;
	}
}
