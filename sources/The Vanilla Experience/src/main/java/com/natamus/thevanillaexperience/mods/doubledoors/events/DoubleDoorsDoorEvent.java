/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.1.
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

package com.natamus.thevanillaexperience.mods.doubledoors.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.thevanillaexperience.mods.doubledoors.config.DoubleDoorsConfigHandler;
import com.natamus.thevanillaexperience.mods.doubledoors.util.DoubleDoorsUtil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.StoneButtonBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.WoodButtonBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class DoubleDoorsDoorEvent {
	private static List<BlockPos> prevpoweredpos = new ArrayList<BlockPos>();
	private static HashMap<BlockPos, Integer> prevbuttonpos = new HashMap<BlockPos, Integer>();
	
	@SubscribeEvent
	public void onNeighbourNotice(BlockEvent.NeighborNotifyEvent e) {
		World world = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getWorld());
		if (world == null) {
			return;
		}
		
		BooleanProperty proppowered = BlockStateProperties.POWERED;
		BlockPos pos = e.getPos().toImmutable();
		BlockState state = e.getState();
		Block block = state.getBlock();
		
		if (block instanceof PressurePlateBlock == false) {
			if (block instanceof StoneButtonBlock == false && block instanceof WoodButtonBlock == false) {
				return;
			}
			else {
				if (prevbuttonpos.containsKey(pos)) {
					prevbuttonpos.remove(pos);
				}
				else {
					prevbuttonpos.put(pos, 1);
					return;
				}
				
				if (!state.get(proppowered)) {
					if (!prevpoweredpos.contains(pos)) {
						return;
					}
					prevpoweredpos.remove(pos);
				}
			}
		}
		else {
			if (!state.get(proppowered)) {
				if (!prevpoweredpos.contains(pos)) {
					return;
				}
			}
		}
		
		boolean playsound = true;
		boolean stateprop = state.get(proppowered);
		
		Iterator<BlockPos> blocksaround = BlockPos.getAllInBox(pos.getX()-1, pos.getY(), pos.getZ()-1, pos.getX()+1, pos.getY()+1, pos.getZ()+1).iterator();
		
		BlockPos doorpos = null;
		while (blocksaround.hasNext()) {
			BlockPos npos = blocksaround.next().toImmutable();
			BlockState ostate = world.getBlockState(npos);
			if (DoubleDoorsUtil.isDoorBlock(ostate)) {
				doorpos = npos;
				break;
			}
		}
		
		if (doorpos != null) {
			if (processDoor(world, doorpos, world.getBlockState(doorpos), stateprop, playsound)) {
				if (stateprop) {
					prevpoweredpos.add(pos);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onDoorClick(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		if (world.isRemote && e.getHand().equals(Hand.MAIN_HAND)) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
		if (player.isSneaking()) {
			return;
		}
		
		BlockPos cpos = e.getPos();
		BlockState clickstate = world.getBlockState(cpos);

		if (!DoubleDoorsUtil.isDoorBlock(clickstate)) {
			return;
		}
		if (clickstate.getMaterial().equals(Material.IRON)) {
			return;
		}
		
		if (processDoor(world, cpos, clickstate, null, true)) {
			e.setUseBlock(Result.DENY);
			e.setCanceled(true);		
		}
	}
	
	private boolean processDoor(World world, BlockPos pos, BlockState state, Boolean isopen, Boolean playsound) {
		Block block = state.getBlock();
		if (block instanceof DoorBlock) {
			if (state.get(DoorBlock.HALF).equals(DoubleBlockHalf.UPPER)) {
				pos = pos.down().toImmutable();
				state = world.getBlockState(pos);
			}
		}
		
		if (isopen == null) {
			isopen = !state.get(BlockStateProperties.OPEN);
		}
		
		int yoffset = 0;
		if (block instanceof DoorBlock == false) {
			yoffset = 1;
		}
		
		Iterator<BlockPos> blocksaround = BlockPos.getAllInBox(pos.getX()-1, pos.getY()-1, pos.getZ()-1, pos.getX()+1, pos.getY()+yoffset, pos.getZ()+1).iterator();
		while (blocksaround.hasNext()) {
			BlockPos bpa = blocksaround.next();
			if (bpa.equals(pos)) {
				continue;
			}
			BlockState ostate = world.getBlockState(bpa);
			Block oblock = ostate.getBlock();
			if (DoubleDoorsUtil.isDoorBlock(ostate)) {
				if (oblock.getRegistryName().equals(block.getRegistryName())) {
					if (oblock instanceof DoorBlock) {
						if (!DoubleDoorsConfigHandler.GENERAL.enableDoors.get()) {
							continue;
						}
						
						DoorBlock door = (DoorBlock)oblock;
						if (state.get(DoorBlock.HINGE).equals(ostate.get(DoorBlock.HINGE))) {
							continue;
						}
						
						if (playsound) {
							door.openDoor(world, state, pos, isopen); // toggleDoor
						}
						else {
							world.setBlockState(pos, state.with(DoorBlock.OPEN, Boolean.valueOf(isopen)), 10);
						}
						world.setBlockState(bpa, ostate.with(DoorBlock.OPEN, Boolean.valueOf(isopen)), 10);
						return true;
					}
					else if (oblock instanceof TrapDoorBlock) {
						if (!DoubleDoorsConfigHandler.GENERAL.enableTrapdoors.get()) {
							continue;
						}
						
						if (playsound) {
							if (isopen) {
								int i = ostate.getMaterial() == Material.IRON ? 1037 : 1007;
								world.playEvent(null, i, pos, 0);
							} else {
								int j = ostate.getMaterial() == Material.IRON ? 1036 : 1013;
								world.playEvent(null, j, pos, 0);
							}
						}

						world.setBlockState(pos, state.with(BlockStateProperties.OPEN, Boolean.valueOf(isopen)), 10);
						world.setBlockState(bpa, ostate.with(BlockStateProperties.OPEN, Boolean.valueOf(isopen)), 10);
						return true;
					}
					else if (oblock instanceof FenceGateBlock) {
						if (!DoubleDoorsConfigHandler.GENERAL.enableFenceGates.get()) {
							continue;
						}
						
						world.setBlockState(pos, state.with(DoorBlock.OPEN, Boolean.valueOf(isopen)), 10);
						world.setBlockState(bpa, ostate.with(DoorBlock.OPEN, Boolean.valueOf(isopen)), 10);
						return true;
					}
				}
			}
		}
		
		return false;
	}
}