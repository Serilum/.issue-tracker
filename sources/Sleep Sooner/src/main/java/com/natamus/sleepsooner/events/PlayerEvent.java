/*
 * This is the latest source code of Sleep Sooner.
 * Minecraft version: 1.18.2, mod version: 2.7.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Sleep Sooner ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
		Player player = e.getPlayer();
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
