/*
 * This is the latest source code of Player Tracking.
 * Minecraft version: 1.19.1, mod version: 1.9.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.playertracking.util;

import java.util.ArrayList;
import java.util.List;

import com.natamus.collective.functions.StringFunctions;
import com.natamus.playertracking.Main;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;

public class Tracking {	
	public Tracking(Main instance) {
		this.instance = instance;
	}

	public void setLoc(int x, int y, int z) {
		mx = x;
		my = y;
		mz = z;
	}

	public boolean checkPlayer(Player pl, int x, int z) {
		BlockPos pos = pl.blockPosition();
		
		int num = 0;
		if(x == 0) {
			int plz = pos.getZ();
			num = Math.abs(z);
			if(Math.abs(mz - plz) < num)
				if(z < 0) {
					if(plz < mz)
						return true;
				} else
					if(plz > mz)
						return true;
		} else
			if(z == 0) {
				int plz = pos.getX();
				num = Math.abs(x);
				if(Math.abs(mx - plz) < num)
					if(x < 0) {
						if(plz < mx)
							return true;
					} else
						if(plz > mx)
							return true;
			}
		return false;
	}

	public void TrackDir(Player player, int x, int z, Player player2) {
		String compass = "West";
		List<? extends Player> plist = player.getCommandSenderWorld().players();
		List<Player> in = new ArrayList<Player>();
		int num = Math.abs(x) + Math.abs(z);
		if(player2 == null) {
			for(int i = 0; i < plist.size(); i++)
			{
				Player pl = (Player)plist.get(i);
				boolean can = checkPlayer(pl, x, z);
				if(can)
					in.add(pl);
			}

		} else {
			boolean can = checkPlayer(player2, x, z);
			if(can)
				in.add(player2);
		}
		if(z < 0)
			compass = "North";
		else
			if(z > 0)
				compass = "South";
		if(x < 0)
			compass = "West";
		else
			if(x > 0)
				compass = "East";
		String str = "";
		for(int i = 0; i < in.size(); i++)
			str = (new StringBuilder(String.valueOf(str))).append(((Player)in.get(i)).getName()).append(",").toString();

		StringFunctions.sendMessage(player, compass + "<" + num + ">: " + str, ChatFormatting.GRAY);
	}

	public int findBlock(Level world, int x, int z, Block block1, Block block2) {
		boolean hasmat = true;
		int length = 0;
		for(int i = 1; i < 10000; i++) {
			BlockPos bpos = new BlockPos(mx + x * i, my, mz + z * i);
			Block block = world.getBlockState(bpos).getBlock();

			if(!hasmat)
				continue;
			if(block.equals(block1)) {
				length++;
				if(block1.equals(Blocks.COBBLESTONE))
					world.setBlockAndUpdate(bpos, Blocks.AIR.defaultBlockState());
				continue;
			}
			if(block.equals(block2)) {
				hasmat = false;
				length++;
				if(block1.equals(Blocks.COBBLESTONE))
					world.setBlockAndUpdate(bpos, Blocks.AIR.defaultBlockState());
			} else {
				return 0;
			}
			break;
		}

		if(length > 0 && !hasmat)
			return length;
		else
			return 0;
	}

	public void Track(Block block1, Block block2, Player player, Player player2) {
		Level world = player.getCommandSenderWorld();
		BlockPos bpos = player.blockPosition().below().immutable();
		Block block = world.getBlockState(bpos).getBlock();
		int northDist = findBlock(world, -1, 0, block1, block2);
		int southDist = findBlock(world, 1, 0, block1, block2);
		int eastDist = findBlock(world, 0, -1, block1, block2);
		int westDist = findBlock(world, 0, 1, block1, block2);
		
		StringFunctions.sendMessage(player, "Tracking Data:          Use F3 for directions", ChatFormatting.DARK_GRAY);
		if(northDist > 0)
			TrackDir(player, -northDist * 25, 0, player2);
		if(southDist > 0)
			TrackDir(player, southDist * 25, 0, player2);
		if(eastDist > 0)
			TrackDir(player, 0, -eastDist * 25, player2);
		if(westDist > 0)
			TrackDir(player, 0, westDist * 25, player2);
		if(block.equals(Blocks.OBSIDIAN) && northDist + southDist + eastDist + westDist >= 25)
			world.setBlockAndUpdate(bpos, Blocks.AIR.defaultBlockState());
	}

	public void Track(Player player, Player player2) {
		Level world = player.getCommandSenderWorld();
		BlockPos bpos = player.blockPosition().below().immutable();
		Block block = world.getBlockState(bpos).getBlock();
		if(block.equals(Blocks.DIAMOND_BLOCK)) {
			Track(Blocks.OBSIDIAN, Blocks.GOLD_BLOCK, player, player2);
			return;
		}
		if(block.equals(Blocks.OBSIDIAN)) {
			Track(Blocks.COBBLESTONE, Blocks.STONE, player, player2);
			return;
		}
		
		StringFunctions.sendMessage(player, "You need to be on a tracking block to do that.", ChatFormatting.GRAY);
		StringFunctions.sendMessage(player, "Do '/track help' for more information.", ChatFormatting.GRAY);
	}

	public Main instance;
	int mx;
	int my;
	int mz;
}
