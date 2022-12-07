/*
 * This is the latest source code of Villager Names.
 * Minecraft version: 1.19.3, mod version: 4.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.villagernames.events;

import com.google.gson.JsonSyntaxException;
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

import java.util.HashMap;

public class VillagerEvent {
	public static void onSpawn(Level world, Entity entity) {
		if (!(entity instanceof Villager)) {
			boolean goname = false;
			if (ConfigHandler.nameModdedVillagers) {
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
		if (profession.equals("none") || profession.equals("nitwit")) {
			return InteractionResult.PASS;
		}
		
		if (profession.contains(":")) {
			profession = profession.split(":")[1];
		}
		if (profession.contains("-")) {
			profession = profession.split("-")[0].trim();
		}
		
		Component namecomponent = villager.getName();

		try {
			String json = Component.Serializer.toJson(namecomponent); // {"bold":true,"color":"blue","text":"Hero Villager"}
			HashMap<String, String> map = JsonFunctions.JsonStringToHashMap(json);

			String prevname = map.get("text");
			String upperprofession = profession.substring(0, 1).toUpperCase() + profession.substring(1);

			map.put("text", prevname + " the " + upperprofession);
			villager.setCustomName(Component.Serializer.fromJson(JsonFunctions.HashMapToJsonString(map)));

			new Thread(() -> {
				try {
					Thread.sleep(10);
				} catch (InterruptedException ignored) { }

				map.put("text", prevname.replace(" the ", "").replace(upperprofession, ""));
				villager.setCustomName(Component.Serializer.fromJson(JsonFunctions.HashMapToJsonString(map)));
			}).start();
		}
		catch (JsonSyntaxException ex) {
			return InteractionResult.PASS;
		}
		
		return InteractionResult.PASS;
	}
}
