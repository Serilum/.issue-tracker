/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.thevanillaexperience.mods.sleepsooner.events;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.sleepsooner.config.SleepSoonerConfigHandler;

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
public class SleepSoonerPlayerEvent {
	@SubscribeEvent
	public void playerClick(PlayerInteractEvent.RightClickBlock e) {  
		Player player = e.getPlayer();
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.getHand().equals(InteractionHand.MAIN_HAND)) {
            return;
        }
		
		if (!SleepSoonerConfigHandler.GENERAL.enableSleepSooner.get()) {
			return;
		}
		
		Block block = world.getBlockState(e.getPos()).getBlock();
		if (block instanceof BedBlock == false) {
			return;
		}
		
		Integer sleeptime = SleepSoonerConfigHandler.GENERAL.whenSleepIsPossibleInTicks.get();
		
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

		if (SleepSoonerConfigHandler.GENERAL.enablePreSleepMessage.get()) {
			String unique = GlobalVariables.lingermessages.get(GlobalVariables.random.nextInt(GlobalVariables.lingermessages.size()));
			
			StringFunctions.sendMessage(player, "You " + unique + " until dusk. You may now sleep.", ChatFormatting.DARK_GREEN);
		}
	}
}
