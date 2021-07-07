/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.villagerdeathmessages.events;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.villagerdeathmessages.config.VillagerDeathMessagesConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VillagerDeathMessagesVillagerEvent {
	@SubscribeEvent
	public void villagerDeath(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		boolean goname = false;
		if (entity instanceof VillagerEntity == false) {
			if (VillagerDeathMessagesConfigHandler.GENERAL.mentionModdedVillagers.get()) {
				if (EntityFunctions.isModdedVillager(entity)) {
					goname = true;
				}
			}
			
			if (!goname) {
				return;
			}
		}
		
		String prefix = "";
		
		// Villager profession
		if (goname) {
			// MODDED
			prefix = "A special villager";
			if (entity.hasCustomName()) {
				prefix = entity.getName().getString();
			}
		}
		else {
			VillagerEntity villager = (VillagerEntity)entity;
			VillagerData d = villager.getVillagerData();
			VillagerProfession prof = d.getProfession();
			
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
		if (VillagerDeathMessagesConfigHandler.GENERAL.showLocation.get()) {
			Vector3d loc = entity.position();
			String location = "x=" + (int)loc.x + ", y=" + (int)loc.y + ", z=" + (int)loc.z;
			
			locstring = " at " + location;
		}
		
		StringFunctions.broadcastMessage(world, prefix + " has died" + locstring + " by " + imsourcename + ".", TextFormatting.DARK_GREEN);
	}
}
