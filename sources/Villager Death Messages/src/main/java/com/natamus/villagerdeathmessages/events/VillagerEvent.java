/*
 * This is the latest source code of Villager Death Messages.
 * Minecraft version: 1.19.2, mod version: 2.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.villagerdeathmessages.events;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.villagerdeathmessages.config.ConfigHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VillagerEvent {
	@SubscribeEvent
	public void villagerDeath(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		boolean goname = false;
		if (entity instanceof Villager == false) {
			if (ConfigHandler.GENERAL.mentionModdedVillagers.get()) {
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
		DamageSource source = e.getSource();
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
		if (ConfigHandler.GENERAL.showLocation.get()) {
			Vec3 loc = entity.position();
			String location = "x=" + (int)loc.x + ", y=" + (int)loc.y + ", z=" + (int)loc.z;
			
			locstring = " at " + location;
		}
		
		StringFunctions.broadcastMessage(world, prefix + " has died" + locstring + " by " + imsourcename + ".", ChatFormatting.DARK_GREEN);
	}
}
