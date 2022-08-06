/*
 * This is the latest source code of Sleep Sooner.
 * Minecraft version: 1.19.2, mod version: 2.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.sleepsooner.events;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.sleepsooner.config.ConfigHandler;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PlayerEvent {
	@SubscribeEvent
	public void playerClick(PlayerInteractEvent.RightClickBlock e) {  
		Player player = e.getEntity();
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.getHand().equals(InteractionHand.MAIN_HAND)) {
            return;
        }
		
		if (!ConfigHandler.GENERAL.enableSleepSooner.get()) {
			return;
		}
		
		Block block = world.getBlockState(e.getPos()).getBlock();
		if (block instanceof BedBlock == false) {
			return;
		}
		
		Integer sleeptime = ConfigHandler.GENERAL.whenSleepIsPossibleInTicks.get();
		
		Integer currenttime = (int)world.getDayTime();
		Integer days = (int)Math.floor((double)currenttime/24000);
		Integer daytime = currenttime - (days*24000);
		
		if (sleeptime > 12540) {
			if (daytime > 12540 && daytime < sleeptime) {
				StringFunctions.sendMessage(player, "It's too early to sleep.", ChatFormatting.DARK_GREEN);
				
				e.setCanceled(true);
				return;
			}
		}
		
		if (daytime > 12540) {
			return;
		}
		
		if (daytime < sleeptime) {
			return;
		}
		
		WorldFunctions.setWorldTime((ServerLevel)world, 12540);

		if (ConfigHandler.GENERAL.enablePreSleepMessage.get()) {
			String unique = GlobalVariables.lingermessages.get(GlobalVariables.random.nextInt(GlobalVariables.lingermessages.size()));
			
			StringFunctions.sendMessage(player, "You " + unique + " until dusk. You may now sleep.", ChatFormatting.DARK_GREEN);
		}
	}
}
