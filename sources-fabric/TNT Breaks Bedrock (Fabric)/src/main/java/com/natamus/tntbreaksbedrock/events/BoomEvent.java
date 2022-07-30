/*
 * This is the latest source code of TNT Breaks Bedrock.
 * Minecraft version: 1.19.1, mod version: 2.3.
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

package com.natamus.tntbreaksbedrock.events;

import java.util.ArrayList;
import java.util.List;

import com.natamus.tntbreaksbedrock.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BoomEvent {
	public static void onExplosion(Level world, Entity sourceEntity, Explosion explosion) {
		if (world.isClientSide) {
			return;
		}
		
		List<BlockPos> affected = explosion.getToBlow();
		if (affected.size() == 0) {
			return;
		}
		
		if (sourceEntity instanceof PrimedTnt == false) {
			return;
		}
		
		List<BlockPos> bedrocks = new ArrayList<BlockPos>();
		for (BlockPos pos : affected) {
			for (BlockPos bedpos : Util.getBedrocks(world, pos.immutable())) {
				if (!bedrocks.contains(bedpos.immutable())) {
					bedrocks.add(bedpos.immutable());
				}
			}
		}
		
		BlockState air = Blocks.AIR.defaultBlockState();
		for (BlockPos bedrock : bedrocks) {
			world.setBlockAndUpdate(bedrock, air);
		}
	}
}
