/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.justplayerheads.events;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.HeadFunctions;
import com.natamus.thevanillaexperience.mods.justplayerheads.cmds.CommandJph;
import com.natamus.thevanillaexperience.mods.justplayerheads.config.JustPlayerHeadsConfigHandler;
import com.natamus.thevanillaexperience.mods.justplayerheads.util.JustPlayerHeadsVariables;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class JustPlayerHeadsPlayerEvent {
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent e) {
    	CommandJph.register(e.getDispatcher());
    }
	
	@SubscribeEvent
	public void entityDeath(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (!JustPlayerHeadsConfigHandler.GENERAL.playerDropsHeadOnDeath.get()) {
			return;
		}
		
		if (entity instanceof Player == false) {
			return;
		}
		
		double num = GlobalVariables.random.nextDouble();
		if (num > JustPlayerHeadsConfigHandler.GENERAL.playerHeadDropChance.get()) {
			return;
		}
		
		Player player = (Player)entity;
		String name = player.getName().getString();
		
		ItemStack head = null;
		if (JustPlayerHeadsConfigHandler.GENERAL.enablePlayerHeadCaching.get()) {
			if (JustPlayerHeadsVariables.headcache.containsKey(name.toLowerCase())) {
				head = JustPlayerHeadsVariables.headcache.get(name.toLowerCase());
			}
		}
		
		if (head == null) {
			head = HeadFunctions.getPlayerHead(name, 1);
			
			if (head != null && JustPlayerHeadsConfigHandler.GENERAL.enablePlayerHeadCaching.get()) {
				ItemStack cachehead = head.copy();
				
				JustPlayerHeadsVariables.headcache.put(name.toLowerCase(), cachehead);
			}
		}
		
		if (head == null) {
			return;
		}
		
		player.spawnAtLocation(head, 1);
	}
}
