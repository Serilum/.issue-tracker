/*
 * This is the latest source code of Kelp Fertilizer.
 * Minecraft version: 1.19.2, mod version: 2.3.
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
