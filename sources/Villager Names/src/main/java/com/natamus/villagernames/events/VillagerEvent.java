/*
 * This is the latest source code of Villager Names.
 * Minecraft version: 1.17.1, mod version: 3.0.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Villager Names ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.villagernames.events;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.villagernames.config.ConfigHandler;
import com.natamus.villagernames.util.Names;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VillagerEvent {
	@SubscribeEvent
	public void onSpawn(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		Entity entity = e.getEntity();
		if (entity instanceof Villager == false) {
			boolean goname = false;
			if (ConfigHandler.GENERAL.nameModdedVillagers.get()) {
				if (EntityFunctions.isModdedVillager(entity)) {
					goname = true;
				}
			}
			
			if (!goname) {
				return;
			}
		}
		
		if (!entity.hasCustomName()) {
			EntityFunctions.nameEntity(entity, Names.getRandomName());
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.EntityInteract e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getTarget();
		if (!entity.getClass().equals(Villager.class)) {
			return;
		}
		
		if (!entity.hasCustomName()) {
			return;
		}
		
		Player player = e.getPlayer();
		if (player.isCrouching()) {
			return;
		}
		
		Villager villager = (Villager)entity;
		VillagerData d = villager.getVillagerData();
		
		String profession = d.getProfession().toString();
		if (profession == "none" || profession == "nitwit") {
			return;
		}
		
		if (profession.contains(":")) {
			profession = profession.split(":")[1];
		}
		
		String prevname = villager.getName().getString();
		String upperprofession = profession.substring(0, 1).toUpperCase() + profession.substring(1);
		
		villager.setCustomName(new TextComponent(prevname + " the " + upperprofession));
		new Thread( new Runnable() {
	    	public void run()  {
	        	try  { Thread.sleep( 10 ); }
	            catch (InterruptedException ie)  {}
	        	
	        	villager.setCustomName(new TextComponent(prevname.replace(" the ", "").replace(upperprofession, "")));
	    	}
	    } ).start();
	}
}
