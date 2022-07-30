/*
 * This is the latest source code of Placeable Blaze Rods.
 * Minecraft version: 1.19.1, mod version: 1.6.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Placeable Blaze Rods ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.placeableblazerods.events;

import com.natamus.placeableblazerods.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

public class BlazeRodEvent {
	public static boolean onBlockClick(Level world, Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
		if (world.isClientSide) {
			return true;
		}
		
		ItemStack handstack = player.getItemInHand(hand);
		if (!handstack.getItem().equals(Items.BLAZE_ROD)) {
			return true;
		}

		BlockPos placepos = pos.relative(hitVec.getDirection());
		BlockState targetstate = world.getBlockState(placepos);
		if (!targetstate.getBlock().equals(Blocks.AIR)) {
			return true;
		}

		Direction direction = hitVec.getDirection();
		BlockState blockState = world.getBlockState(placepos.relative(direction.getOpposite()));

		BlockState newstate;
		if (blockState.is(Main.blazerodblock) && blockState.getValue((Property)DirectionalBlock.FACING) == direction)
			newstate = (BlockState)Main.blazerodblock.defaultBlockState().setValue((Property)DirectionalBlock.FACING, (Comparable)direction.getOpposite());
		else {
			newstate = (BlockState)Main.blazerodblock.defaultBlockState().setValue((Property)DirectionalBlock.FACING, (Comparable) direction);
		}

		world.setBlock(placepos, newstate, 2);

		if (!player.isCreative()) {
			handstack.shrink(1);
		}

		player.swing(hand);
		return true;
	}
}