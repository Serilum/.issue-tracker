/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

package com.natamus.thevanillaexperience.mods.villagernames.events;

import com.natamus.collective.functions.EntityFunctions;
import com.natamus.thevanillaexperience.mods.villagernames.config.VillagerNamesConfigHandler;
import com.natamus.thevanillaexperience.mods.villagernames.util.Names;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class VillagerNamesVillagerEvent {
	@SubscribeEvent
	public void onSpawn(EntityJoinWorldEvent e) {
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		Entity entity = e.getEntity();
		if (entity instanceof VillagerEntity == false) {
			boolean goname = false;
			if (VillagerNamesConfigHandler.GENERAL.nameModdedVillagers.get()) {
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
		World world = e.getWorld();
		if (world.isRemote) {
			return;
		}
		
		Entity entity = e.getTarget();
		if (!entity.getClass().equals(VillagerEntity.class)) {
			return;
		}
		
		if (!entity.hasCustomName()) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
		if (player.isCrouching()) {
			return;
		}
		
		VillagerEntity villager = (VillagerEntity)entity;
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
		
		villager.setCustomName(new StringTextComponent(prevname + " the " + upperprofession));
		new Thread( new Runnable() {
	    	public void run()  {
	        	try  { Thread.sleep( 10 ); }
	            catch (InterruptedException ie)  {}
	        	
	        	villager.setCustomName(new StringTextComponent(prevname.replace(" the ", "").replace(upperprofession, "")));
	    	}
	    } ).start();
	}
}
