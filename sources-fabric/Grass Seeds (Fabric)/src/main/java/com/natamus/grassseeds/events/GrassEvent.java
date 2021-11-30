/*
 * This is the latest source code of Grass Seeds.
 * Minecraft version: 1.18.x, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Grass Seeds ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.grassseeds.events;

import com.natamus.collective_fabric.functions.BlockPosFunctions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.HitResult;

public class GrassEvent {
	public static InteractionResult onDirtClick(Player player, Level world, InteractionHand hand, HitResult hitResult) {
		if (world.isClientSide) {
			return InteractionResult.PASS;
		}
		
		ItemStack handstack = player.getItemInHand(hand);
		if (!handstack.getItem().equals(Items.WHEAT_SEEDS)) {
			return InteractionResult.PASS;
		}
		
		BlockPos cpos = BlockPosFunctions.getBlockPosFromHitResult(hitResult).below();
		Block block = world.getBlockState(cpos).getBlock();
		if (block.equals(Blocks.DIRT)) {
			world.setBlockAndUpdate(cpos, Blocks.GRASS_BLOCK.defaultBlockState());
		}
		else if (block.equals(Blocks.GRASS_BLOCK)) {
			BlockPos up = cpos.above();
			if (world.getBlockState(up).getBlock().equals(Blocks.AIR)) {
				world.setBlockAndUpdate(up, Blocks.GRASS.defaultBlockState());
			}
			else if (world.getBlockState(up).getBlock().equals(Blocks.GRASS)) {
				upgradeGrass(world, up);
			}
			else {
				return InteractionResult.PASS;
			}
		}
		else if (block.equals(Blocks.GRASS)) {
			upgradeGrass(world, cpos);
		}
		else {
			return InteractionResult.PASS;
		}
		
		if (!player.isCreative()) {
			handstack.shrink(1);
		}
		
		return InteractionResult.SUCCESS;
	}
	
	private static void upgradeGrass(Level world, BlockPos pos) {
	      DoublePlantBlock blockdoubleplant = (DoublePlantBlock)Blocks.TALL_GRASS;
	      BlockState doubleplantstate = blockdoubleplant.defaultBlockState();
	      if (doubleplantstate.canSurvive(world, pos) && world.isEmptyBlock(pos.above())) {
	         world.setBlock(pos, (BlockState)blockdoubleplant.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 2);
	         world.setBlock(pos.above(), (BlockState)blockdoubleplant.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
	      }
	}
}
