/*
 * This is the latest source code of Kelp Fertilizer.
 * Minecraft version: 1.19.x, mod version: 1.9.
 *
 * If you'd like access to the source code of previous Minecraft versions or previous mod versions, consider becoming a Github Sponsor or Patron.
 * You'll be added to a private repository which contains all versions' source of Kelp Fertilizer ever released, along with some other perks.
 *
 * Github Sponsor link: https://github.com/sponsors/ricksouth
 * Patreon link: https://patreon.com/ricksouth
 *
 * Becoming a Sponsor or Patron allows me to dedicate more time to the development of mods.
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.kelpfertilizer.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BehaviourKelpDispenser implements DispenseItemBehavior {
	protected final Item kelp;

	public BehaviourKelpDispenser(Item itemIn){
		kelp = itemIn;
	}

	@Override
	public ItemStack dispense(BlockSource source, ItemStack itemstack) {
		Level world = source.getLevel();
		if (world.isClientSide) {
			return itemstack;
		}
		
		BlockPos pos = source.getPos();
		BlockState state = source.getBlockState();
		Direction facing = state.getValue(DispenserBlock.FACING);
		BlockPos facepos = pos.relative(facing);
		
		if (BoneMealItem.growCrop(itemstack, world, facepos)) {
			world.levelEvent(2005, facepos, 0);
		}
		
		return itemstack;
	}
}
