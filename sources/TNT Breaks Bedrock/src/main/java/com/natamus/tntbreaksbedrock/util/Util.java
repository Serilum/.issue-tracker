/*
 * This is the latest source code of TNT Breaks Bedrock.
 * Minecraft version: 1.19.2, mod version: 2.1.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.tntbreaksbedrock.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class Util {
	public static List<BlockPos> getBedrocks(Level world, BlockPos p) {
		List<BlockPos> bedrocks = new ArrayList<BlockPos>();
		
		Iterator<BlockPos> it = BlockPos.betweenClosedStream(p.getX(), p.getY()-1, p.getZ(), p.getX(), p.getY()+1, p.getZ()).iterator();
		while (it.hasNext()) {
			BlockPos bp = it.next().immutable();
			if (world.getBlockState(bp).getBlock().equals(Blocks.BEDROCK)) {
				bedrocks.add(bp);
			}
		}
		
		return bedrocks;
	}
}
