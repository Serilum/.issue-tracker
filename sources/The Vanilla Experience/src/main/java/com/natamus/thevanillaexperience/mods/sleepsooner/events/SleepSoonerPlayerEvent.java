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

package com.natamus.thevanillaexperience.mods.sleepsooner.events;

import com.natamus.collective.data.GlobalVariables;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.sleepsooner.config.SleepSoonerConfigHandler;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SleepSoonerPlayerEvent {
	@SubscribeEvent
	public void playerClick(PlayerInteractEvent.RightClickBlock e) {  
		PlayerEntity player = e.getPlayer();
		World world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.getHand().equals(Hand.MAIN_HAND)) {
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
				StringFunctions.sendMessage(player, "It's too early to sleep.", TextFormatting.DARK_GREEN);
				
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
		
		WorldFunctions.setWorldTime((ServerWorld)world, 12540);

		if (SleepSoonerConfigHandler.GENERAL.enablePreSleepMessage.get()) {
			String unique = GlobalVariables.lingermessages.get(GlobalVariables.random.nextInt(GlobalVariables.lingermessages.size()));
			
			StringFunctions.sendMessage(player, "You " + unique + " until dusk. You may now sleep.", TextFormatting.DARK_GREEN);
		}
	}
}
