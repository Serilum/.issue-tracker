/*
 * This is the latest source code of Villager Names.
 * Minecraft version: 1.17.x, mod version: 3.3.
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

import java.util.HashMap;

import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.collective_fabric.functions.JsonFunctions;
import com.natamus.villagernames.config.ConfigHandler;
import com.natamus.villagernames.util.Names;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class VillagerEvent {
	public static void onSpawn(Level world, Entity entity) {
		if (entity instanceof Villager == false) {
			boolean goname = false;
			if (ConfigHandler.nameModdedVillagers.getValue()) {
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
	
	public static InteractionResult onVillagerInteract(Player player, Level world, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
		if (!entity.getClass().equals(Villager.class)) {
			return InteractionResult.PASS;
		}
		
		if (!entity.hasCustomName()) {
			return InteractionResult.PASS;
		}
		
		if (player.isCrouching()) {
			return InteractionResult.PASS;
		}
		
		Villager villager = (Villager)entity;
		VillagerData d = villager.getVillagerData();
		
		String profession = d.getProfession().toString();
		if (profession == "none" || profession == "nitwit") {
			return InteractionResult.PASS;
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
		
		return InteractionResult.PASS;
	}
}
