/*
 * This is the latest source code of Placeable Blaze Rods.
 * Minecraft version: 1.19.3, mod version: 1.4.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.placeableblazerods.events;

import com.natamus.placeableblazerods.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BlazeRodEvent {
	@SubscribeEvent
	public void onBlockClick(PlayerInteractEvent.RightClickBlock e) {
		Level level = e.getLevel();
		if (level.isClientSide) {
			return;
		}

		ItemStack handstack = e.getItemStack();
		if (!handstack.getItem().equals(Items.BLAZE_ROD)) {
			return;
		}

		BlockPos pos = e.getPos();
		BlockHitResult hitVec = e.getHitVec();

		BlockPos placepos = pos.relative(hitVec.getDirection());
		BlockState targetstate = level.getBlockState(placepos);
		if (!targetstate.getBlock().equals(Blocks.AIR)) {
			return;
		}

		Direction direction = hitVec.getDirection();
		BlockState blockState = level.getBlockState(placepos.relative(direction.getOpposite()));

		BlockState newstate;
		if (blockState.is(Main.BLAZE_ROD_BLOCK) && blockState.getValue((Property)DirectionalBlock.FACING) == direction)
			newstate = (BlockState)Main.BLAZE_ROD_BLOCK.defaultBlockState().setValue((Property)DirectionalBlock.FACING, (Comparable)direction.getOpposite());
		else {
			newstate = (BlockState)Main.BLAZE_ROD_BLOCK.defaultBlockState().setValue((Property)DirectionalBlock.FACING, (Comparable) direction);
		}

		level.setBlock(placepos, newstate, 2);

		Player player = e.getEntity();
		if (!player.isCreative()) {
			handstack.shrink(1);
		}

		player.swing(e.getHand());
	}
}