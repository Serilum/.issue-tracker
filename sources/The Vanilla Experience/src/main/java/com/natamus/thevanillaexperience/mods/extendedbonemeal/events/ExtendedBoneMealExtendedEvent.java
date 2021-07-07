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

package com.natamus.thevanillaexperience.mods.extendedbonemeal.events;

import com.natamus.collective.functions.CropFunctions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.MushroomBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ExtendedBoneMealExtendedEvent {
	@SubscribeEvent
	public void onBoneMeal(BonemealEvent e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
		if (!player.isShiftKeyDown()) {
			return;
		}
		
		ItemStack hand = player.getItemInHand(Hand.MAIN_HAND);
		if (!hand.getItem().equals(Items.BONE_MEAL)) {
			return;
		}
		
		BlockPos cpos = e.getPos();
		BlockState state = world.getBlockState(cpos);
		Block block = state.getBlock();
		if (block instanceof IGrowable == false || block instanceof SaplingBlock || block.equals(Blocks.GRASS_BLOCK) || block instanceof TallGrassBlock || block instanceof MushroomBlock) { // || block.equals(Blocks.sap) || block.equals(Blocks.GRASS)) {
			return;
		}
		
		if (CropFunctions.growCrop(world, player, state, cpos)) {
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onNetherwartClick(PlayerInteractEvent.RightClickBlock e) {
		World world = e.getWorld();
		if (world.isClientSide) {
			return;
		}
		
		PlayerEntity player = e.getPlayer();
		ItemStack hand = e.getItemStack();
		if (!hand.getItem().equals(Items.BONE_MEAL)) {
			return;
		}
		
		BlockPos targetpos = e.getPos();
		BlockState state = world.getBlockState(targetpos);
		Block block = state.getBlock();
		if (block.equals(Blocks.NETHER_WART)) {
			if (!CropFunctions.growCrop(world, player, state, targetpos)) {
				
				return;
			}
		}
		else if (block.equals(Blocks.CACTUS)) {
			if (!CropFunctions.growCactus(world, targetpos)) {
				return;
			}
		}
		else if (block.equals(Blocks.SUGAR_CANE)) {
			if (!CropFunctions.growSugarcane(world, targetpos)) {
				return;
			}
		}
		else if (block.equals(Blocks.VINE)) {
			if (!CropFunctions.growVine(world, targetpos)) {
				return;
			}
		}
		else {
			return;
		}
		
		if (!player.isCreative()) {
			hand.shrink(1);
		}
	}
}