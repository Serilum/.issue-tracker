/*
 * This is the latest source code of Underwater Enchanting.
 * Minecraft version: 1.19.2, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.underwaterenchanting.events;

import java.util.Iterator;

import com.natamus.collective.functions.NumberFunctions;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class EnhantmentEvent {
	@SubscribeEvent
	public void onEnchanting(EnchantmentLevelSetEvent e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		int row = e.getEnchantRow();
		int currentpower = e.getPower();
		
		if (currentpower == 0) {
			int bookshelfcount = 0;
			
			BlockPos epos = e.getPos();
			Iterator<BlockPos> it = BlockPos.betweenClosedStream(epos.getX()-2, epos.getY()-2, epos.getZ()-2, epos.getX()+2, epos.getY()+2, epos.getZ()+2).iterator();
			while (it.hasNext()) {
				BlockPos np = it.next();
				if (world.getBlockState(np).getBlock().equals(Blocks.BOOKSHELF)) {
					bookshelfcount++;
				}
			}
			
			if (bookshelfcount > 0) {
				int level = NumberFunctions.getEnchantingTableLevel(row, bookshelfcount);
				if (level < 0) {
					return;
				}
				
				e.setEnchantLevel(level);
			}
		}
	}
}
