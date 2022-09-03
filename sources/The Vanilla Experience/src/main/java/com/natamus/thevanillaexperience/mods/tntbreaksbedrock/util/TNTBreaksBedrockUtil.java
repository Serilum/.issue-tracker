/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.thevanillaexperience.mods.tntbreaksbedrock.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class TNTBreaksBedrockUtil {
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
