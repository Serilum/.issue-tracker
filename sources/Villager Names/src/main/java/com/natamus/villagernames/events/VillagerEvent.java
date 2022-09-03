/*
 * This is the latest source code of Villager Names.
 * Minecraft version: 1.19.2, mod version: 3.7.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.villagernames.events;

import java.util.HashMap;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.JsonFunctions;
import com.natamus.villagernames.config.ConfigHandler;
import com.natamus.villagernames.util.Names;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VillagerEvent {
	@SubscribeEvent
	public void onSpawn(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
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
		Level world = e.getLevel();
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
		
		Player player = e.getEntity();
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
		if (profession.contains("-")) {
			profession = profession.split("-")[0].trim();
		}
		
		Component namecomponent = villager.getName();
		String json = Component.Serializer.toJson(namecomponent); // {"bold":true,"color":"blue","text":"Hero Villager"}
		HashMap<String, String> map = JsonFunctions.JsonStringToHashMap(json);
		
		String prevname = map.get("text");
		String upperprofession = profession.substring(0, 1).toUpperCase() + profession.substring(1);
		
		map.put("text", prevname + " the " + upperprofession);
		villager.setCustomName(Component.Serializer.fromJson(JsonFunctions.HashMapToJsonString(map)));
		
		new Thread( new Runnable() {
	    	public void run()  {
	        	try  { Thread.sleep( 10 ); }
	            catch (InterruptedException ie)  {}
	        	
	        	map.put("text", prevname.replace(" the ", "").replace(upperprofession, ""));
	    		villager.setCustomName(Component.Serializer.fromJson(JsonFunctions.HashMapToJsonString(map)));
	    	}
	    } ).start();
	}
}
