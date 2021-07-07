/*
 * This is the latest source code of Player Death Kick.
 * Minecraft version: 1.16.5, mod version: 1.4.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Player Death Kick ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.playerdeathkick.events;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.playerdeathkick.config.ConfigHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DeathEvent {
	@SubscribeEvent
	public void onDeathEvent(LivingDeathEvent e) {
		Entity entity = e.getEntity();
		World world = entity.getCommandSenderWorld();
		if (world.isClientSide) {
			return;
		}
		
		if (entity instanceof ServerPlayerEntity == false) {
			return;
		}
		
		ServerPlayerEntity serverplayer = (ServerPlayerEntity)entity;
		
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
		
		serverplayer.connection.disconnect(new StringTextComponent(deathmessage));
		
		if (ConfigHandler.GENERAL.broadcastKickToServer.get()) {
			String playername = serverplayer.getName().getString();
			StringFunctions.broadcastMessage(world, "The player " + playername + " has died and been kicked from the server.", TextFormatting.DARK_GRAY);
		}
	}
}
