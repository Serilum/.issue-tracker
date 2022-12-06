/*
 * This is the latest source code of Starter Kit.
 * Minecraft version: 1.19.2, mod version: 4.1.
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

package com.natamus.starterkit.events;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.natamus.collective.functions.PlayerFunctions;
import com.natamus.starterkit.config.ConfigHandler;
import com.natamus.starterkit.util.Reference;
import com.natamus.starterkit.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.TickTask;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class FirstSpawnEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onSpawn(EntityJoinLevelEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		Entity entity = e.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		
		final Player player = (Player)entity;
		world.getServer().tell(new TickTask(world.getServer().getTickCount(), () -> {
			if (PlayerFunctions.isJoiningWorldForTheFirstTime(player, Reference.MOD_ID, false)) {
				Util.setStarterKit(player);
			}
		}));
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
				
				new Thread(() -> {
					try  { Thread.sleep( 2000 ); }
					catch (InterruptedException ignored)  {}

					Util.setStarterKit(player);
				}).start();
			}
		}
	}
}
