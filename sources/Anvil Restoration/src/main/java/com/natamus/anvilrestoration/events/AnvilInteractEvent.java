/*
 * This is the latest source code of Anvil Restoration.
 * Minecraft version: 1.18.1, mod version: 1.2.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Anvil Restoration ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.anvilrestoration.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AnvilInteractEvent {
	@SubscribeEvent
	public void onAnvilClick(PlayerInteractEvent.RightClickBlock e) {
		Level world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		ItemStack hand = e.getItemStack();
		Item item = hand.getItem();
		if (!item.equals(Items.IRON_INGOT) && !item.equals(Items.OBSIDIAN)) {
			return;
		}
		
		BlockPos cpos = e.getPos();
		BlockState state = world.getBlockState(cpos);
		Block block = state.getBlock();
		
		BlockState newstate;
		if (block.equals(Blocks.ANVIL) && item.equals(Items.OBSIDIAN)) {
			newstate = Blocks.CHIPPED_ANVIL.defaultBlockState();
		}
		else if (block.equals(Blocks.CHIPPED_ANVIL)) {
			if (item.equals(Items.IRON_INGOT)) {
				newstate = Blocks.ANVIL.defaultBlockState();
			}
			else { // obsidian
				newstate = Blocks.DAMAGED_ANVIL.defaultBlockState();
			}
		}
		else if (block.equals(Blocks.DAMAGED_ANVIL) && item.equals(Items.IRON_INGOT)) {
			newstate = Blocks.CHIPPED_ANVIL.defaultBlockState();
		}
		else {
			return;
		}
		
		Direction rotation = state.getValue(AnvilBlock.FACING);
		world.setBlock(cpos, newstate.setValue(AnvilBlock.FACING, rotation), 3);
		world.playSound(null, cpos.getX(), cpos.getY(), cpos.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 0.5F, 1.0F);
		
		e.setCanceled(true);
		
		Player player = e.getPlayer();
		if (!player.isCreative() && item.equals(Items.IRON_INGOT)) {
			hand.shrink(1);
		}
	}
}
