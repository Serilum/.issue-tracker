/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.16.5, mod version: 1.2.
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

package com.natamus.thevanillaexperience.mods.grassseeds.events;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GrassSeedsGrassEvent {
	@SubscribeEvent
	public void onDirtClick(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		if (!hand.getItem().equals(Items.WHEAT_SEEDS)) {
			return;
		}
		
		BlockPos cpos = e.getPos();
		Block block = world.getBlockState(cpos).getBlock();
		if (block.equals(Blocks.DIRT)) {
			hand.shrink(1);
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
			hand.shrink(1);
		}
		else if (block.equals(Blocks.GRASS)) {
			upgradeGrass(world, cpos);
			hand.shrink(1);
		}
	}
	
	public void upgradeGrass(World world, BlockPos pos) {
	      DoublePlantBlock blockdoubleplant = (DoublePlantBlock)Blocks.TALL_GRASS;
	      if (blockdoubleplant.defaultBlockState().canSurvive(world, pos) && world.isEmptyBlock(pos.above())) {
	         blockdoubleplant.placeAt(world, pos, 2);
	      }
	}
}
