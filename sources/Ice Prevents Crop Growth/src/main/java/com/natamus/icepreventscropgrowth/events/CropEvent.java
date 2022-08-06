/*
 * This is the latest source code of Ice Prevents Crop Growth.
 * Minecraft version: 1.19.2, mod version: 1.7.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.icepreventscropgrowth.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.BlockEvent.CropGrowEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class CropEvent {
	private final static List<Block> iceblocks = new ArrayList<Block>(Arrays.asList(Blocks.ICE, Blocks.FROSTED_ICE, Blocks.BLUE_ICE, Blocks.PACKED_ICE));
	
	@SubscribeEvent
	public void mobItemDrop(CropGrowEvent.Pre e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		BlockPos pos = e.getPos();
		Block belowblock = world.getBlockState(pos.below(2)).getBlock();
		
		if (BlockFunctions.isOneOfBlocks(iceblocks, belowblock)) {
			e.setResult(Result.DENY);
		}
	}
}
