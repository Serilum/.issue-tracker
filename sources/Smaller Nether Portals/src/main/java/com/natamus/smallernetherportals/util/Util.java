/*
 * This is the latest source code of Smaller Nether Portals.
 * Minecraft version: 1.16.5, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Smaller Nether Portals ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.smallernetherportals.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PortalSize;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

public class Util {
	public static void processSmallerPortal(World world, BlockPos pos) {
		BlockPos bottomleft;
		String airdirection = "none";
		Rotation rotation = null;
		
		if (isObsidian(world.getBlockState(pos.east()))) {
			if (isAir(world.getBlockState(pos.west()))) {
				bottomleft = pos.west();
				airdirection = "east";
			}
			else {
				bottomleft = pos;
				rotation = Rotation.CLOCKWISE_180;
			}
		}
		else if (isObsidian(world.getBlockState(pos.west()))) {
			if (isAir(world.getBlockState(pos.east()))) {
				airdirection = "east";
			}
			else {
				rotation = Rotation.CLOCKWISE_180;
			}
			bottomleft = pos;
		}
		else if (isObsidian(world.getBlockState(pos.north()))) {
			if (isAir(world.getBlockState(pos.south()))) {
				airdirection = "south";
			}
			else {
				rotation = Rotation.CLOCKWISE_90;
			}
			bottomleft = pos;
		}
		else if (isObsidian(world.getBlockState(pos.south()))) {
			if (isAir(world.getBlockState(pos.north()))) {
				bottomleft = pos.north();
				airdirection = "south";
			}
			else {
				bottomleft = pos;
				rotation = Rotation.CLOCKWISE_90;
			}
		}
		else {
			return;
		}
		bottomleft = bottomleft.toImmutable();
		
		int height = -1;
		if (!world.getBlockState(bottomleft.down()).getBlock().equals(Blocks.OBSIDIAN)) {
			return;
		}
		
		if (world.getBlockState(bottomleft.up(2)).getBlock().equals(Blocks.OBSIDIAN)) {
			height = 2;
		}
		else if (world.getBlockState(bottomleft.up(3)).getBlock().equals(Blocks.OBSIDIAN)) {
			height = 3;
		}
		else {
			return;
		}
		
		List<BlockPos> toportals = new ArrayList<BlockPos>();
		
		int heighti;
		for (heighti = height; heighti > 0; heighti--) {
			toportals.add(bottomleft.up(heighti-1).toImmutable());
			if (airdirection != "none") {
				if (airdirection == "south") {
					if (!world.getBlockState(bottomleft.north()).getBlock().equals(Blocks.OBSIDIAN)) {
						break;
					}
					
					Block wblock = world.getBlockState(bottomleft.south()).getBlock();
					if (isAir(wblock)) {
						if (!world.getBlockState(bottomleft.south(2)).getBlock().equals(Blocks.OBSIDIAN)) {
							break;
						}
						toportals.add(bottomleft.up(heighti-1).south().toImmutable());
					}
					else if (!wblock.equals(Blocks.OBSIDIAN)) {
						break;
					}
				}
				else if (airdirection == "east") {
					if (!world.getBlockState(bottomleft.west()).getBlock().equals(Blocks.OBSIDIAN)) {
						break;
					}
					
					Block wblock = world.getBlockState(bottomleft.east()).getBlock();
					if (isAir(wblock)) {
						if (!world.getBlockState(bottomleft.east(2)).getBlock().equals(Blocks.OBSIDIAN)) {
							break;
						}
						toportals.add(bottomleft.up(heighti-1).east().toImmutable());
					}
					else if (!wblock.equals(Blocks.OBSIDIAN)) {
						break;
					}
				}
			}
			
		}

		if (heighti == 0) {
			if (rotation == null) {
				if (airdirection == "east") {
					rotation = Rotation.CLOCKWISE_180;
				}
				else {
					rotation = Rotation.CLOCKWISE_90;
				}
			}
			
			int obsidiancount = 0;
			
			for (BlockPos tp0 : toportals) {
				for (BlockPos tp0around : getBlocksAround(tp0.toImmutable(), rotation)) {
					Block tp0block = world.getBlockState(tp0around).getBlock();
					if (isObsidian(tp0block)) {
						obsidiancount++;
					}
				}
			}
			
			if (toportals.size() == 2) {
				if (obsidiancount < 6) {
					return;
				}
			}
			else {
				if (obsidiancount < 8) {
					return;
				}
			}
			
			BlockPos portalpos = null;
			for (BlockPos tp : toportals) {
				world.setBlockState(tp, Blocks.NETHER_PORTAL.getDefaultState().rotate(world, tp, rotation), 2);
				portalpos = tp;
			}
			
			if (portalpos == null) {
				return;
			}
			
			Axis axis = null;
			if (airdirection == "east" ) {
				axis = Axis.X;
			}
			else {
				axis = Axis.Z;
			}
			
			PortalSize size = new PortalSize(world, portalpos, axis);
	        MinecraftForge.EVENT_BUS.post(
                new BlockEvent.PortalSpawnEvent(
                    world,
                    portalpos,
                    world.getBlockState(portalpos),
                    size
                )
            );
		}
	}
	
	public static List<BlockPos> getBlocksAround(BlockPos pos, Rotation rot) {
		List<BlockPos> around = new ArrayList<BlockPos>();
		BlockPos impos = pos.toImmutable();
		
		around.add(impos.up().toImmutable());
		around.add(impos.down().toImmutable());
		
		if (rot.equals(Rotation.CLOCKWISE_90)) {
			around.add(impos.north().toImmutable());
			around.add(impos.south().toImmutable());
		}
		else {
			around.add(impos.east().toImmutable());
			around.add(impos.west().toImmutable());
		}
		
		return around;
	}
	
	public static List<BlockPos> getFrontBlocks(World world, BlockPos portalblock) {
		List<BlockPos> returnblocks = new ArrayList<BlockPos>();
		
		Boolean smallest = false;
		if (isObsidian(world.getBlockState(portalblock.east())) && isObsidian(world.getBlockState(portalblock.west()))) {
			smallest = true;
		}
		else if (isObsidian(world.getBlockState(portalblock.north())) && isObsidian(world.getBlockState(portalblock.south()))) {
			smallest = true;
		}
		else {
			if (isPortal(world.getBlockState(portalblock.north()))) {
				portalblock = portalblock.north().toImmutable();
			}
			else if (isPortal(world.getBlockState(portalblock.west()))) {
				portalblock = portalblock.west().toImmutable();
			}
		}
		
		if (!isPortalOrObsidian(world.getBlockState(portalblock.west()))) {
			returnblocks.add(portalblock.west().down().toImmutable());
			if (!smallest) {
				returnblocks.add(portalblock.west().south().down().toImmutable());
			}
		}
		else {
			returnblocks.add(portalblock.south().down().toImmutable());
			if (!smallest) {
				returnblocks.add(portalblock.south().east().down().toImmutable());
			}
		}
		
		return returnblocks;
	}
	
	public static BlockPos findPortalAround(World world, BlockPos pos) {
		BlockPos portalpos = null;
		
		for (int i = 0; i < 10; i++) {
			BlockPos cpos = pos.up(i).toImmutable();
			Iterator<BlockPos> around = BlockPos.getAllInBox(cpos.getX()-1, cpos.getY(), cpos.getZ()-1, cpos.getX()+1, cpos.getY(), cpos.getZ()+1).iterator();
			while (around.hasNext()) {
				BlockPos ap = around.next();
				if (isPortal(world.getBlockState(ap))) {
					portalpos = ap.toImmutable();
					break;
				}
			}
			if (portalpos != null) {
				break;
			}
		}
		return portalpos;
	}
	
	public static void setObsidian(World world, List<BlockPos> toblocks) {
		for (BlockPos tbs : toblocks) {
			if (shouldMakeFront(world.getBlockState(tbs))) {
				world.setBlockState(tbs, Blocks.OBSIDIAN.getDefaultState());
			}
			for (int i = 1; i < 3; i++) {
				BlockPos up = tbs.up(i);
				if (!isAir(world.getBlockState(up))) {
					world.setBlockState(up, Blocks.AIR.getDefaultState());
				}
			}
		}
	}
	
	public static Boolean shouldMakeFront(BlockState bs) {
		Block block = bs.getBlock();
		if (isAir(block) || block.equals(Blocks.NETHERRACK) || block.equals(Blocks.SOUL_SAND)) {
			return true;
		}
		return false;
	}
	
	public static Boolean isObsidian(BlockState bs) {
		Block block = bs.getBlock();
		if (block.equals(Blocks.OBSIDIAN)) {
			return true;
		}
		return false;
	}
	public static Boolean isObsidian(Block block) {
		if (block.equals(Blocks.OBSIDIAN)) {
			return true;
		}
		return false;
	}
	
	public static Boolean isAir(BlockState bs) {
		Block block = bs.getBlock();
		if (block.equals(Blocks.AIR) || block.equals(Blocks.FIRE)) {
			return true;
		}
		return false;
	}
	public static Boolean isAir(Block block) {
		if (block.equals(Blocks.AIR) || block.equals(Blocks.FIRE)) {
			return true;
		}
		return false;
	}
	
	public static Boolean isPortalOrObsidian(BlockState bs) {
		Block block = bs.getBlock();
		if (block.equals(Blocks.NETHER_PORTAL) || block.equals(Blocks.OBSIDIAN)) {
			return true;
		}
		return false;
	}
	
	public static Boolean isPortal(BlockState bs) {
		Block block = bs.getBlock();
		if (block.equals(Blocks.NETHER_PORTAL)) {
			return true;
		}
		return false;
	}
}
