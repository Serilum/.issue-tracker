/*
 * This is the latest source code of Player Death Kick.
 * Minecraft version: 1.19.3, mod version: 1.8.
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

package com.natamus.playerdeathkick.events;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.playerdeathkick.config.ConfigHandler;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DeathEvent {
	@SubscribeEvent
	public void onDeathEvent(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		Level world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof ServerPlayer == false) {
			return;
		}
		
		ServerPlayer serverplayer = (ServerPlayer)entity;
		
		if (ConfigHandler.GENERAL.exemptAdminPlayers.get()) {
			if (serverplayer.hasPermissions(2)) {
				return;
			}
		}
		
		String deathmessage = ConfigHandler.GENERAL.disconnectMessage.get();
		
		if (ConfigHandler.GENERAL.addDeathCauseToMessage.get()) {
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
			else {
				if (truesource != null) {
					imsourcename = "a " +  sourcename;
				}
			}
			
			deathmessage = deathmessage.replace("%death%", imsourcename);
		}
		
		serverplayer.connection.disconnect(Component.literal(deathmessage));
		
		if (ConfigHandler.GENERAL.broadcastKickToServer.get()) {
			String playername = serverplayer.getName().getString();
			StringFunctions.broadcastMessage(world, "The player " + playername + " has died and been kicked from the server.", ChatFormatting.DARK_GRAY);
		}
	}
}
