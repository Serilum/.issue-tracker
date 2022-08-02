/*
 * This is the latest source code of Hoe Tweaks.
 * Minecraft version: 1.19.1, mod version: 1.6.
 *
 * Please don't distribute without permission.
 * For all modding projects, feel free to visit the CurseForge page: https://curseforge.com/members/serilum/projects
 */

package com.natamus.hoetweaks.util;

import java.util.Iterator;

import com.natamus.collective.functions.BlockFunctions;
import com.natamus.collective.functions.CompareBlockFunctions;
import com.natamus.hoetweaks.config.ConfigHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;

public class Util {
	public static int getHoeRange(ItemStack stack) {
		HoeItem item = (HoeItem)stack.getItem();
		Tier tier = item.getTier();
		if (tier == null) {
			return ConfigHandler.GENERAL.woodenTierHoeRange.get();
		}
		
		String tierstring = tier.toString().toLowerCase();
		
		int range = ConfigHandler.GENERAL.woodenTierHoeRange.get();
		if (tier.equals(Tiers.STONE)) {
			range = ConfigHandler.GENERAL.stoneTierHoeRange.get();
		}
		else if (tier.equals(Tiers.GOLD)) {
			range = ConfigHandler.GENERAL.goldTierHoeRange.get();
		}
		else if (tier.equals(Tiers.IRON) || tierstring.equals("steel")) {
			range = ConfigHandler.GENERAL.ironTierHoeRange.get();
		}
		else if (tier.equals(Tiers.DIAMOND)) {
			range = ConfigHandler.GENERAL.diamondTierHoeRange.get();
		}
		else if (tier.equals(Tiers.NETHERITE)) {
			range = ConfigHandler.GENERAL.netheriteTierHoeRange.get();
		}
		
		return range;
	}
	
	public static int processSoilGetDamage(Level world, BlockPos pos, int range, Block blocktoset, boolean checkforfarmland) {
		int damage = 0;
		
		Iterator<BlockPos> blockstotill = BlockPos.betweenClosedStream(pos.getX()-range, pos.getY(), pos.getZ()-range, pos.getX()+range, pos.getY(), pos.getZ()+range).iterator();
		while(blockstotill.hasNext()) {
			BlockPos apos = blockstotill.next();
			Block ablock = world.getBlockState(apos).getBlock();
			if (!checkforfarmland && !CompareBlockFunctions.isDirtBlock(ablock)) {
				continue;
			}
			else if (checkforfarmland && !ablock.equals(Blocks.FARMLAND)) {
				continue;
			}
			
			BlockPos posabove = apos.above();
			Block blockabove = world.getBlockState(posabove).getBlock();
			if (blockabove instanceof BushBlock) {
				BlockFunctions.dropBlock(world, posabove);
			}
			else if (!blockabove.equals(Blocks.AIR)) {
				continue;
			}
			
			world.setBlock(apos, blocktoset.defaultBlockState(), 3);
			damage += 1;
		}
		
		return damage;
	}
}
