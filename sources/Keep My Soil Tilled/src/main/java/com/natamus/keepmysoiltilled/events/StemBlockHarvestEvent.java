/*
 * This is the latest source code of Keep My Soil Tilled.
 * Minecraft version: 1.19.1, mod version: 1.3.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.keepmysoiltilled.events;

import java.util.Iterator;

import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class StemBlockHarvestEvent {
	@SubscribeEvent
	public void onCropBlockBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (world == null) {
			return;
		}
		
		BlockState state = e.getState();
		if (state.getBlock() instanceof StemGrownBlock) {
			BlockPos pos = e.getPos();
			
			BlockState farmstate = null;
			boolean foundfarmland = false;
			
			Iterator<BlockPos> blocksaroundbelow = BlockPos.betweenClosed(pos.getX()-1, pos.getY()-1, pos.getZ()-1, pos.getX()+1, pos.getY()-1, pos.getZ()+1).iterator();
			while (blocksaroundbelow.hasNext()) {
				BlockPos np = blocksaroundbelow.next();
				Block nb = world.getBlockState(np).getBlock();
				if (nb instanceof FarmBlock) {
					foundfarmland = true;
					farmstate = nb.defaultBlockState();
					break;
				}
			}
			
			if (foundfarmland && farmstate != null) {
				world.setBlock(pos.below(), farmstate, 3);
			}
		}
	}
}
