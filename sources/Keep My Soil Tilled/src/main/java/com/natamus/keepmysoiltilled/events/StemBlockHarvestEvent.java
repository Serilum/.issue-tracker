/*
 * This is the latest source code of Keep My Soil Tilled.
 * Minecraft version: 1.18.1, mod version: 1.1.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Keep My Soil Tilled ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
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
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class StemBlockHarvestEvent {
	@SubscribeEvent
	public void onCropBlockBreak(BlockEvent.BreakEvent e) {
		Level world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
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
