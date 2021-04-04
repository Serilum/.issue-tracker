/*
 * This is the latest source code of Bamboo Spreads.
 * Minecraft version: 1.16.5, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Bamboo Spreads ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.bamboospreads.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.natamus.bamboospreads.variables.BlockVariables;
import com.natamus.collective.functions.WorldFunctions;

import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class PlaceEvent {
	private static HashMap<World, List<BlockPos>> bambootoreplace = new HashMap<World, List<BlockPos>>();
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e) {
		World world = e.world;
		if (world.isRemote || !e.phase.equals(Phase.END)) {
			return;
		}
		
		if (bambootoreplace.get(world).size() > 0) {
			BlockPos bamboo = bambootoreplace.get(world).get(0);
			
			BlockState state = world.getBlockState(bamboo);
			if (state.getBlock() instanceof BambooBlock) {
				world.setBlockState(bamboo, BlockVariables.SPREADING_BAMBOO_BLOCK.getDefaultState());
			}
			
			bambootoreplace.get(world).remove(0);
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		bambootoreplace.put(world, new ArrayList<BlockPos>());
	}
	
	@SubscribeEvent
	public void onWorldTick(BlockEvent.EntityPlaceEvent e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BlockState state = e.getPlacedBlock();
		if (state.getBlock() instanceof BambooBlock) {
			BlockPos pos = e.getPos();
			bambootoreplace.get(world).add(pos.toImmutable());
		}
	}
}
