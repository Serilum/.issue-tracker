/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of The Vanilla Experience ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.thevanillaexperience.mods.tntbreaksbedrock.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.natamus.thevanillaexperience.mods.tntbreaksbedrock.util.TNTBreaksBedrockUtil;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class TNTBreaksBedrockBoomEvent {
	@SubscribeEvent
	public void onExplosion(ExplosionEvent.Detonate e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		Explosion explosion = e.getExplosion();
		Vector3d exvec = explosion.getPosition();
		
		List<BlockPos> affected = explosion.getToBlow();
		if (affected.size() == 0) {
			return;
		}
		
		Boolean found = false;
		
		Iterator<Entity> eit = world.getEntities(null, new AxisAlignedBB(exvec.x-2, exvec.y-2, exvec.z-2, exvec.x+2, exvec.y+2, exvec.z+2)).iterator();
		while (eit.hasNext()) {
			Entity ne = eit.next();
			if (ne instanceof TNTEntity) {
				TNTEntity tnt = (TNTEntity)ne;
				int fuseleft = tnt.getLife();
				if (fuseleft < 5) {
					found = true;
					break;
				}
			}
		}
		
		if (!found) {
			return;
		}
		
		List<BlockPos> bedrocks = new ArrayList<BlockPos>();
		for (BlockPos pos : affected) {
			for (BlockPos bedpos : TNTBreaksBedrockUtil.getBedrocks(world, pos.immutable())) {
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
