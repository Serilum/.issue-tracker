/*
 * This is the latest source code of Grass Seeds.
 * Minecraft version: 1.19.2, mod version: 2.2.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 */

package com.natamus.grassseeds.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GrassEvent {
	@SubscribeEvent
	public void onDirtClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getLevel();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (!hand.getItem().equals(Items.WHEAT_SEEDS)) {
			return;
		}
		
		Player player = e.getEntity();
		BlockPos cpos = e.getPos();
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
				return;
			}
		}
		else if (block.equals(Blocks.GRASS)) {
			upgradeGrass(world, cpos);
		}
		else {
			return;
		}
		
		if (!player.isCreative()) {
			hand.shrink(1);
		}
	}
	
	public void upgradeGrass(Level world, BlockPos pos) {
	      DoublePlantBlock blockdoubleplant = (DoublePlantBlock)Blocks.TALL_GRASS;
	      BlockState doubleplantstate = blockdoubleplant.defaultBlockState();
	      if (doubleplantstate.canSurvive(world, pos) && world.isEmptyBlock(pos.above())) {
	         DoublePlantBlock.placeAt(world, doubleplantstate, pos, 2);
	      }
	}
}
