/*
 * This is the latest source code of TNT Breaks Bedrock.
 * Minecraft version: 1.18.0, mod version: 1.8.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of TNT Breaks Bedrock ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
