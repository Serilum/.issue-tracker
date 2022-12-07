/*
 * This is the latest source code of Villager Death Messages.
 * Minecraft version: 1.19.3, mod version: 2.9.
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

package com.natamus.villagerdeathmessages.events;

import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.collective_fabric.functions.StringFunctions;
import com.natamus.villagerdeathmessages.config.ConfigHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VillagerEvent {
	public static void villagerDeath(Level world, Entity entity, DamageSource source) {
		if (world.isClientSide) {
			return;
		}
		
		boolean goname = false;
		if (entity instanceof Villager == false) {
			if (ConfigHandler.mentionModdedVillagers) {
				if (EntityFunctions.isModdedVillager(entity)) {
					goname = true;
				}
			}
			
			if (!goname) {
				return;
			}
		}
		
		boolean modded = false;
		String prefix = "";
		
		if (goname) {
			modded = true;
		}
		else {
			Villager villager = (Villager)entity;
			VillagerData d = villager.getVillagerData();
			VillagerProfession prof = d.getProfession();
			
			if (prof != null) {
				String profession = "";
				if (prof.toString() != "none") {
					profession = prof.toString();
				}
				
				if (profession != "") {
					prefix = "A " + profession + " villager";
					if (villager.hasCustomName()) {
						prefix = villager.getName().getString() + " the " + profession;
					}
				}
				else {
					prefix = "A villager";
					if (villager.hasCustomName()) {
						prefix = villager.getName().getString();
					}
				}
			}
			else {
				modded = true;
			}
		}
		
		if (modded) {
			prefix = "A special villager";
			if (entity.hasCustomName()) {
				prefix = entity.getName().getString();
			}
		}
		
		// Damage source
		String imsourcename = source.getMsgId();
		String sourcename = "";
		
		Entity truesource = source.getEntity();
		if (truesource != null) {
			sourcename = truesource.getName().getString();
		}
		if (sourcename != "" && imsourcename.equals("player")) {
			imsourcename = sourcename;
		}
		else if (imsourcename.contains(".")) {
			imsourcename = imsourcename.split("\\.")[0];
		}
		
		// Position	
		String locstring = "";
		if (ConfigHandler.showLocation) {
			Vec3 loc = entity.position();
			String location = "x=" + (int)loc.x + ", y=" + (int)loc.y + ", z=" + (int)loc.z;
			
			locstring = " at " + location;
		}
		
		StringFunctions.broadcastMessage(world, prefix + " has died" + locstring + " by " + imsourcename + ".", ChatFormatting.DARK_GREEN);
	}
}
