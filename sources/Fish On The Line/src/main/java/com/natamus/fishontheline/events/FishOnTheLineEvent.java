/*
 * This is the latest source code of Fish On The Line.
 * Minecraft version: 1.16.5, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Fish On The Line ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.fishontheline.events;

import java.util.HashMap;
import java.util.List;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.fishontheline.config.ConfigHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.EntityDataManager.DataEntry;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FishOnTheLineEvent {
	private static HashMap<String, Integer> sounddelay = new HashMap<String, Integer>();
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		PlayerEntity player = e.player;
		World world = player.getEntityWorld();
		if (world.isRemote || !e.phase.equals(Phase.START)) {
			return;
		}
		
		FishingBobberEntity fbe = player.fishingBobber;
		if (fbe == null) {
			return;
		}
		
		if (ConfigHandler.GENERAL.mustHoldBellInOffhand.get()) {
			ItemStack offhandstack = player.getHeldItemOffhand();
			if (!offhandstack.getItem().equals(Items.BELL)) {
				return;
			}
		}
		
		boolean fishontheline = false;
		int booleancount = 0;
		
		EntityDataManager datamanager = fbe.getDataManager();
		List<DataEntry<?>> entries = datamanager.getAll();
		for (DataEntry<?> entry : entries) {
			String entryvalue = entry.getValue().toString();
			if (entryvalue.equalsIgnoreCase("true") || entryvalue.equalsIgnoreCase("false")) {
				if (booleancount >= 3) {
					if (entryvalue.equalsIgnoreCase("true")) {
						fishontheline = true;
					}
				}
				
				booleancount += 1;
			}
		}
		
		if (fishontheline) {
			int delay = 0;
			
			String playername = player.getName().getString();
			if (sounddelay.containsKey(playername)) {
				delay = sounddelay.get(playername);
			}
			
			if (delay == 0) {
				world.playSound((PlayerEntity)null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 0.5F, 0.4F / (GlobalVariables.random.nextFloat() * 0.4F + 0.8F));
				delay = 7;
			}
			else {
				delay -= 1;
			}
			
			sounddelay.put(playername, delay);
		}
	}
}
