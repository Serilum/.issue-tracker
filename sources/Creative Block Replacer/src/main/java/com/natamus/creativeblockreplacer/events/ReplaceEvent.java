/*
 * This is the latest source code of Creative Block Replacer.
 * Minecraft version: 1.19.2, mod version: 1.7.
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

package com.natamus.creativeblockreplacer.events;

import java.util.HashMap;
import java.util.Map;

import com.natamus.collective.functions.StringFunctions;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ReplaceEvent {
	private static Map<String, Boolean> replacingplayers = new HashMap<String, Boolean>();
	private static Map<String, Integer> sneaktotal = new HashMap<String, Integer>();
	private static Map<String, Integer> sneakcurrent = new HashMap<String, Integer>();
	private static Map<String, BlockPos> lastpos = new HashMap<String, BlockPos>();
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent e) {
		Player player = e.player;
		Level world = player.getCommandSenderWorld();
		if (world.isClientSide || !e.phase.equals(Phase.START)) {
			return;
		}
		if (!player.isCreative()) {
			return;
		}
		
		String playername = player.getName().getString();
		BlockPos playerpos = player.blockPosition();
		if (lastpos.containsKey(playername)) {
			if (sneaktotal.containsKey(playername)) {
				if (!lastpos.get(playername).equals(playerpos)) {
					sneaktotal.remove(playername);
					if (sneakcurrent.containsKey(playername)) {
						sneakcurrent.remove(playername);
					}
				}
			}
		}
		lastpos.put(playername, playerpos);
		
		if (player.isShiftKeyDown()) {
			if (sneakcurrent.containsKey(playername)) {
				return;
			}
			int totalsneak = 0;
			if (sneaktotal.containsKey(playername)) {
				totalsneak = sneaktotal.get(playername);
			}
			sneaktotal.put(playername, totalsneak+1);
			sneakcurrent.put(playername, totalsneak+1);
			return;
		}
		
		if (!sneakcurrent.containsKey(playername)) {
			return;
		}
		
		int current = sneakcurrent.get(playername);
		if (current > 2) {
			sneaktotal.put(playername, 0);
			sneakcurrent.remove(playername);
			
			if (replacingplayers.containsKey(playername)) {
				boolean isreplacing = replacingplayers.get(playername);
				if (isreplacing) {
					replacingplayers.put(playername, false);
					StringFunctions.sendMessage(player, "Replacing block mode disabled.", ChatFormatting.YELLOW);
					return;
				}
			}
			
			replacingplayers.put(playername, true);
			StringFunctions.sendMessage(player, "Replacing block mode enabled", ChatFormatting.YELLOW);
			return;
		}
		sneakcurrent.remove(playername);
	}
	
	@SubscribeEvent
	public void onBlockClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getLevel();
		if (world.isClientSide || !e.getHand().equals(InteractionHand.MAIN_HAND)) {
			return;
		}
		
		Player player = e.getEntity();
		if (!player.isCreative()) {
			return;
		}
		String playername = player.getName().getString();
		if (!replacingplayers.containsKey(playername)) {
			return;
		}
		
		boolean isreplacing = replacingplayers.get(playername);
		if (!isreplacing) {
			return;
		}
		
		BlockPos cpos = e.getPos();
		ItemStack hand = e.getItemStack();
		
		Block toblock = Block.byItem(hand.getItem());
		BlockState tostate = toblock.defaultBlockState();
		
		world.setBlockAndUpdate(cpos, tostate);
		e.setCanceled(true);
	}
}
