/*
 * This is the latest source code of Keep My Soil Tilled.
 * Minecraft version: 1.19.2, mod version: 1.5.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.keepmysoiltilled.events;

import java.util.Iterator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StemBlockHarvestEvent {
	public static void onCropBlockBreak(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
		if (world.isClientSide) {
			return;
		}
		
		if (state.getBlock() instanceof StemGrownBlock) {
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
