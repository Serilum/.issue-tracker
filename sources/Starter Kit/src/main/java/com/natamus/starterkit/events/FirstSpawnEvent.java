/*
 * This is the latest source code of Starter Kit.
 * Minecraft version: 1.18.1, mod version: 3.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Starter Kit ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.starterkit.events;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.starterkit.config.ConfigHandler;
import com.natamus.starterkit.util.Reference;
import com.natamus.starterkit.util.Util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FirstSpawnEvent {
	@SubscribeEvent
	public void onSpawn(EntityJoinWorldEvent e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Player == false) {
			return;
		}
		
		Player player = (Player)entity;
		if (PlayerFunctions.isJoiningWorldForTheFirstTime(player, Reference.MOD_ID)) {
			Util.setStarterKit(player);
		}
	}
	
	@SubscribeEvent
	public void onCommand(CommandEvent e) {
		if (!ConfigHandler.GENERAL.enableFTBIslandCreateCompatibility.get()) {
			return;
		}
		
		ParseResults<CommandSourceStack> results = e.getParseResults();
		CommandContextBuilder<CommandSourceStack> context = results.getContext();
		Command<CommandSourceStack> command = context.getCommand();
		if (command == null) {
			return;
		}
		
		String cmdstr = command.toString().toLowerCase();
		if (cmdstr.contains("ftbteamislands.commands.createislandcommand")) {
			CommandSourceStack source = context.getSource();
			
			Entity sourceentity = source.getEntity();
			if (sourceentity instanceof Player) {
				Player player = (Player)sourceentity;
				
				new Thread( new Runnable() {
			        public void run()  {
			            try  { Thread.sleep( 2000 ); }
			            catch (InterruptedException ie)  {}
			            
			    		Inventory inv = player.getInventory();
			    		boolean isempty = true;
			    		for (int i=0; i < 36; i++) {
			    			if (!inv.getItem(i).isEmpty()) {
			    				isempty = false;
			    				break;
			    			}
			    		}
			    		
			    		if (isempty) {
			    			Util.setStarterKit(player);
			    		}
			        }
			    } ).start();
			}
		}
	}
}
